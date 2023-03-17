package com.example.weatherapplication.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherapplication.model.Weather

@Dao
interface WeatherDao {

    @get:Query("SELECT * FROM weather WHERE locationId = 'id'")
    val getCurrentWeather : LiveData<Weather>

    @Query("SELECT * FROM weather WHERE locationId = 'id'")
    suspend fun getStoredWeather() : Weather

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrentWeather(weather : Weather)
}