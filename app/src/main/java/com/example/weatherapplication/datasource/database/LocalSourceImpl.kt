package com.example.weatherapplication.datasource.database

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.weatherapplication.model.FavoriteWeather
import com.example.weatherapplication.model.OpenWeather

class LocalSourceImpl(private val context: Context):LocalSourceInterface {

    private val weatherDao : WeatherDao by lazy {
        val db  = AppDatabase.getInstance(context)
        db.getWeatherDao()
    }
    override suspend fun getAllFavoriteWeather() : LiveData<List<FavoriteWeather>> {
        return weatherDao.getAllFavoriteWeather()
    }
    override suspend fun selectAllStoredWeatherModel(currentTimeZone: String): LiveData<OpenWeather> {
        return weatherDao.selectWeatherModel(currentTimeZone)
    }

    override suspend fun insertWeatherModel(openWeather: OpenWeather) {
        weatherDao.insertWeatherModel(openWeather)
    }

    override suspend fun deleteFavoritePlace(favoriteWeather: FavoriteWeather) {
        weatherDao.deleteFavoritePlace(favoriteWeather)
    }

    override suspend fun insertFavoritePlace(favoriteWeather: FavoriteWeather) {
        weatherDao.insertFavoritePlace(favoriteWeather)
    }

    override suspend fun updateFavoritePlace(favoriteWeather: FavoriteWeather) {
       weatherDao.updateFavoritePlace(favoriteWeather)
    }


}