package com.hardi.weatherapp.data.api

import com.hardi.weatherapp.data.model.Weather
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface WeatherApi {

    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("q") q: String
    ) : Weather

}