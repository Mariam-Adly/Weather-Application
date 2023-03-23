package com.example.weatherapplication.model.database

import android.content.Context
import com.example.weatherapplication.model.OpenWeather
import com.example.weatherapplication.model.WeatherAlert
import kotlinx.coroutines.flow.Flow

interface LocalDataSourceInterface {
    suspend fun insertWeather(openWeather: OpenWeather):Long

    suspend fun getCurrentWeatherZone(
        id: Int,
        isFavourite: Boolean
    ): OpenWeather

    suspend fun getWeather(lat: Double, long: Double): OpenWeather

    suspend fun updateWeather(openWeather: OpenWeather)
    fun getContext(): Context

    // for fav
    suspend fun getFavWeathersZone(

    ): List<OpenWeather>

    suspend fun deleteWeather(openWeather: OpenWeather)
    suspend fun deleteFavWeather(
        id: Int,
    )

    //for Alerts
    suspend fun insertWeatherAlert(weatherAlert: WeatherAlert):Long
    fun getWeatherAlerts(): Flow<List<WeatherAlert>>
    suspend fun deleteWeatherAlert(id: Int)
    suspend fun getWeatherAlert(id:Int):WeatherAlert

}