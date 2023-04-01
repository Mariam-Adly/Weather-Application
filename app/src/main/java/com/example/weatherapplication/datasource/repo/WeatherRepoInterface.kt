package com.example.weatherapplication.datasource.repo


import androidx.lifecycle.LiveData
import com.example.weatherapplication.model.Alert
import com.example.weatherapplication.model.FavoriteWeather
import com.example.weatherapplication.model.OpenWeather
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface WeatherRepoInterface {

    suspend fun getCurrentTempData(
        lat: Double, long: Double, lang: String="eng", tempUnit: String="metric"
    ): Response<OpenWeather>

    suspend fun selectAllStoredWeatherModel(currentTimeZone: String): LiveData<OpenWeather>
    suspend fun insertWeatherModel(openWeather: OpenWeather)

    suspend fun getAllFavoriteWeather() : LiveData<List<FavoriteWeather>>
    suspend fun deleteFavoritePlace(favoriteWeather: FavoriteWeather)
    suspend fun insertFavoritePlace(favoriteWeather: FavoriteWeather)
    suspend fun updateFavoritePlace(favoriteWeather: FavoriteWeather)
    suspend fun getFavWeatherData(favWeather : FavoriteWeather) :Response<OpenWeather>
    suspend fun getAlerts(): LiveData<List<Alert>>

    suspend fun insertAlert(myAlert: Alert)

    suspend fun deleteAlerts(myAlert: Alert)
}