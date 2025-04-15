package com.hardi.weatherapp.data.model

data class Current(
    val humidity: String,
    val feelslike_c: String,
    val uv: String,
    val cloud: String,
    val wind_mph: String,
    val gust_mph: String,
    val vis_miles: String,
    val precip_mm: String,
    val temp_c: String,
    val condition: Condition
)