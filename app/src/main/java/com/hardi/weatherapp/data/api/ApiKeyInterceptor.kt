package com.hardi.weatherapp.data.api

import com.hardi.weatherapp.di.WeatherApiKey
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor(@WeatherApiKey private val apiKey: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder().header("key", apiKey)
        val request = requestBuilder.build()
        return chain.proceed(request)
    }

}