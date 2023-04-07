package com.example.weatherapplication.datasource.database

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import com.example.weatherapplication.model.Alert
import com.example.weatherapplication.model.FavoriteWeather
import com.example.weatherapplication.model.OpenWeather

class LocalSourceImpl(private val context: Context):LocalSourceInterface {

    private val weatherDao : WeatherDao by lazy {
        val db  = AppDatabase.getInstance(context)
        db.getWeatherDao()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: LocalSourceImpl? = null
        fun getInstance(
             context: Context
        ): LocalSourceImpl {
            return INSTANCE ?: synchronized(this) {
                val instance = LocalSourceImpl( context)
                INSTANCE = instance
                instance
            }
        }
    }
    override suspend fun getAllFavoriteWeather() : LiveData<List<FavoriteWeather>> {
        return weatherDao.getAllFavoriteWeather()
    }

    override suspend fun selectAllStoredWeatherModel(): OpenWeather {
        return weatherDao.selectWeatherModel()
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
//    override val allAlarmsList: List<Alert>
//        get() = weatherDao.allAlarmsList

    override suspend fun updateFavoritePlace(favoriteWeather: FavoriteWeather) {
       weatherDao.updateFavoritePlace(favoriteWeather)
    }

    override suspend fun getAlerts(): LiveData<List<Alert>> {
       return weatherDao.getAlerts()
    }

    override suspend fun insertAlert(myAlert: Alert) {
       weatherDao.insertAlert(myAlert)
    }

    override suspend fun deleteAlerts(myAlert: Alert) {
        weatherDao.deleteAlerts(myAlert)
    }



}