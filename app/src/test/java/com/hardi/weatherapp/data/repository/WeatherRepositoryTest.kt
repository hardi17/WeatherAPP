package com.hardi.weatherapp.data.repository

import app.cash.turbine.test
import com.hardi.weatherapp.data.api.WeatherApi
import com.hardi.weatherapp.data.model.Condition
import com.hardi.weatherapp.data.model.Current
import com.hardi.weatherapp.data.model.Location
import com.hardi.weatherapp.data.model.Weather
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import java.lang.RuntimeException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class WeatherRepositoryTest {

    @Mock
    private lateinit var weatherApi: WeatherApi

    private lateinit var testWeatherRepository : WeatherRepository

    @Before
    fun setUp(){
        testWeatherRepository = WeatherRepository(weatherApi)
    }

    @Test
    fun getWeather_details_success(){
        runTest {
            val query = "London"
            val condition = Condition(
                    "Sunny", "test", "test"
            )
            val current = Current(
                cloud = "test",
                condition = condition,
                humidity = "test",
                feelslike_c = "test",
                uv = "test",
                wind_mph = "test",
                gust_mph ="test",
                vis_miles = "test",
                precip_mm = "test",
                temp_c = "test"
            )
            val location = Location(
                name = "london",
                country = "United Kingdom"
            )
            val weather = Weather(current, location)

            doReturn(weather).`when`(weatherApi).getCurrentWeather(query)
            testWeatherRepository.getWeatherDetails(query).test {
                assertEquals(weather, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            verify(weatherApi, times(1)).getCurrentWeather(query)
        }
    }

    @Test
    fun getWeather_details_error(){
        runTest {
            val query = "t"

            doThrow(RuntimeException("HTTP 401")).`when`(weatherApi).getCurrentWeather(query)
            testWeatherRepository.getWeatherDetails(query).test {
                assertEquals("HTTP 401", awaitError().message)
            }

            verify(weatherApi, times(1)).getCurrentWeather(query)

        }

    }

}