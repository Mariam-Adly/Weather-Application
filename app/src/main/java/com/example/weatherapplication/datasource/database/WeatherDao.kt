package com.example.weatherapplication.datasource.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherapplication.model.Alert
import com.example.weatherapplication.model.FavoriteWeather
import com.example.weatherapplication.model.OpenWeather
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather")
    fun selectWeatherModel(): Flow<OpenWeather>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeatherModel(openWeather: OpenWeather)

    @Query("SELECT * FROM favoriteWeather")
    fun getAllFavoriteWeather() : Flow<List<FavoriteWeather>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoritePlace(favoriteWeather: FavoriteWeather)

    @Delete
   fun deleteFavoritePlace(favoriteWeather: FavoriteWeather)

    @Update
    fun updateFavoritePlace(favoriteWeather: FavoriteWeather)

    @Query("SELECT * FROM Alert")
    fun getAlerts(): Flow<List<Alert>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insertAlert(myAlert: Alert)

    @Delete
     fun deleteAlerts(myAlert: Alert)
}