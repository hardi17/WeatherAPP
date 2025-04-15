package com.hardi.weatherapp.ui.screens

import app.cash.turbine.test
import com.hardi.weatherapp.data.model.Condition
import com.hardi.weatherapp.data.model.Current
import com.hardi.weatherapp.data.model.Location
import com.hardi.weatherapp.data.model.Weather
import com.hardi.weatherapp.data.repository.WeatherRepository
import com.hardi.weatherapp.utils.DispatcherProvider
import com.hardi.weatherapp.utils.TestDispatcherProvider
import com.hardi.weatherapp.utils.Uistate
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class WeatherViewModelTest {

    @Mock
    private lateinit var weatherRepository: WeatherRepository


    private lateinit var dispatcherProvider: DispatcherProvider


    @Before
    fun setUp(){
        dispatcherProvider = TestDispatcherProvider()
    }

    @Test
    fun testSearch_query_UiState_Success(){
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

            doReturn(flowOf(weather)).
            `when`(weatherRepository).
            getWeatherDetails(query)

            val viewModel = WeatherViewModel(weatherRepository, dispatcherProvider)
            viewModel.searchQuery(query)
            viewModel.uistate.test {
                assertEquals(Uistate.Success(weather), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            verify(weatherRepository, times(1)).getWeatherDetails(query)
        }
    }

    @Test
    fun testSearch_query_UiState_Error() {
        runTest {
            val query = "t"
            val error = IllegalStateException("HTTP 401")

            doReturn(flow<Weather> {
                throw error
            }).
            `when`(weatherRepository).
            getWeatherDetails(query)


            val viewModel = WeatherViewModel(weatherRepository, dispatcherProvider)
            viewModel.searchQuery(query)
            viewModel.uistate.test {
                assertEquals(
                    Uistate.Error<Weather>(error,null).toString(),
                    awaitItem().toString()
                )
                cancelAndIgnoreRemainingEvents()
            }
            verify(weatherRepository, times(1)).getWeatherDetails(query)

        }
    }

}