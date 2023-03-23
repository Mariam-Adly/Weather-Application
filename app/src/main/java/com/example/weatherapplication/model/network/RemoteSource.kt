package com.example.weatherapplication.model.network

import com.example.weatherapplication.model.OpenWeather
import retrofit2.Response

interface RemoteSource {

    suspend fun getCurrentTempData(
        lat: Double,
        long: Double,
        lang: String,
        tempUnit: String
    ): Response<OpenWeather>

}