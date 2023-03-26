package com.example.weatherapplication.datasource.database


import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherapplication.model.FavoriteWeather
import com.example.weatherapplication.model.OpenWeather
import com.example.weatherapplication.utility.Converters

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather WHERE timezone = :currentTimeZone")
    fun selectWeatherModel(currentTimeZone: String): LiveData<OpenWeather>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeatherModel(openWeather: OpenWeather)

    @Query("SELECT * FROM favoriteWeather")
    fun getAllFavoriteWeather() : LiveData<List<FavoriteWeather>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoritePlace(favoriteWeather: FavoriteWeather)

    @Delete
   fun deleteFavoritePlace(favoriteWeather: FavoriteWeather)

    @Update
    fun updateFavoritePlace(favoriteWeather: FavoriteWeather)
}