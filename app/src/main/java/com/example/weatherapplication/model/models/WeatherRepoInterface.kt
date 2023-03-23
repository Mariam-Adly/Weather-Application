package com.example.weatherapplication.model.models


import com.example.weatherapplication.model.OpenWeather
import retrofit2.Response

interface WeatherRepoInterface {

    suspend fun getCurrentTempData(
        lat: Double, long: Double, lang: String="eng", tempUnit: String="metric"
    ): Response<OpenWeather>
}