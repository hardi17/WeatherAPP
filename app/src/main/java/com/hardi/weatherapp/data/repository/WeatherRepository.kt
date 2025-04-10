package com.hardi.weatherapp.data.repository

import com.hardi.weatherapp.data.api.WeatherApi
import com.hardi.weatherapp.data.model.Weather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(private val weatherApi: WeatherApi){

    fun getWeatherDetails(query: String) : Flow<Weather> {
        return flow {
            val result = weatherApi.getCurrentWeather(query)
            emit(result)
        }
    }
}