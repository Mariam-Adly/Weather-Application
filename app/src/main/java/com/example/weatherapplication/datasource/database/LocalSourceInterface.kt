package com.example.weatherapplication.datasource.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.weatherapplication.model.Alert
import com.example.weatherapplication.model.FavoriteWeather
import com.example.weatherapplication.model.OpenWeather
import kotlinx.coroutines.flow.Flow

interface LocalSourceInterface {

    suspend fun getAllFavoriteWeather() : LiveData<List<FavoriteWeather>>

    suspend fun selectAllStoredWeatherModel(currentTimeZone: String): LiveData<OpenWeather>
   suspend fun insertWeatherModel(openWeather: OpenWeather)
   suspend fun deleteFavoritePlace(favoriteWeather: FavoriteWeather)
   suspend fun insertFavoritePlace(favoriteWeather: FavoriteWeather)
   suspend fun updateFavoritePlace(favoriteWeather: FavoriteWeather)
   suspend fun getAlerts(): LiveData<List<Alert>>

    suspend fun insertAlert(myAlert: Alert)

    suspend fun deleteAlerts(myAlert: Alert)
}