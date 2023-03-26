package com.example.weatherapplication.datasource.database

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.weatherapplication.model.FavoriteWeather
import com.example.weatherapplication.model.OpenWeather
import com.example.weatherapplication.model.WeatherAlert
import kotlinx.coroutines.flow.Flow

interface LocalSourceInterface {

    suspend fun getAllFavoriteWeather() : LiveData<List<FavoriteWeather>>

    suspend fun selectAllStoredWeatherModel(currentTimeZone: String): LiveData<OpenWeather>
   suspend fun insertWeatherModel(openWeather: OpenWeather)
   suspend fun deleteFavoritePlace(favoriteWeather: FavoriteWeather)
   suspend fun insertFavoritePlace(favoriteWeather: FavoriteWeather)
   suspend fun updateFavoritePlace(favoriteWeather: FavoriteWeather)
}