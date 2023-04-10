package com.example.weatherapplication.datasource

import com.example.weatherapplication.datasource.network.RemoteSource
import com.example.weatherapplication.model.FavoriteWeather
import com.example.weatherapplication.model.OpenWeather
import retrofit2.Response

class FakeRemoteDataSource(private var openWeather: Response<OpenWeather>): RemoteSource {

    override suspend fun getCurrentTempData(
        lat: Double,
        long: Double,
        lang: String,
        tempUnit: String
    ): Response<OpenWeather> {
        return openWeather
    }

    override suspend fun getFavWeatherData(favoriteWeather: FavoriteWeather): Response<OpenWeather> {
        return openWeather
    }
}