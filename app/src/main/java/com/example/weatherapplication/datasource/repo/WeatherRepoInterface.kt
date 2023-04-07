package com.example.weatherapplication.datasource.repo


import androidx.lifecycle.LiveData
import com.example.weatherapplication.model.Alert
import com.example.weatherapplication.model.FavoriteWeather
import com.example.weatherapplication.model.OpenWeather
import retrofit2.Response

interface WeatherRepoInterface {

    suspend fun getCurrentTempData(
        lat: Double, long: Double, lang: String="eng", tempUnit: String="metric"
    ): Response<OpenWeather>

    suspend fun selectAllStoredWeatherModel(): OpenWeather
    suspend fun insertWeatherModel(openWeather: OpenWeather)

    suspend fun getAllFavoriteWeather() : LiveData<List<FavoriteWeather>>
    suspend fun deleteFavoritePlace(favoriteWeather: FavoriteWeather)
    suspend fun insertFavoritePlace(favoriteWeather: FavoriteWeather)
    suspend fun updateFavoritePlace(favoriteWeather: FavoriteWeather)
    suspend fun getFavWeatherData(favWeather : FavoriteWeather) :Response<OpenWeather>
    suspend fun getAlerts(): LiveData<List<Alert>>
   // val allAlarmsList : Single<List<Alert>>?

    suspend fun setAlarm(myAlert: Alert)

    suspend fun deleteAlerts(myAlert: Alert)
}