package com.example.weatherapplication.datasource.repo


import com.example.weatherapplication.model.Alert
import com.example.weatherapplication.model.FavoriteWeather
import com.example.weatherapplication.model.OpenWeather
import retrofit2.Response
import kotlinx.coroutines.flow.*

interface WeatherRepoInterface {

    suspend fun getCurrentTempData(
        lat: Double, long: Double, lang: String="eng", tempUnit: String="metric"
    ): Flow<Response<OpenWeather>>

    suspend fun selectAllStoredWeatherModel(): Flow<OpenWeather>
    suspend fun insertWeatherModel(openWeather: OpenWeather)
    suspend fun getCurrentWeatherForAlert(lat:Double , lon: Double) : Response<OpenWeather>
    suspend fun getAllFavoriteWeather() : Flow<List<FavoriteWeather>>
    suspend fun deleteFavoritePlace(favoriteWeather: FavoriteWeather)
    suspend fun insertFavoritePlace(favoriteWeather: FavoriteWeather)
    suspend fun updateFavoritePlace(favoriteWeather: FavoriteWeather)
    suspend fun getFavWeatherData(favWeather : FavoriteWeather) :Response<OpenWeather>
    suspend fun getAlerts(): Flow<List<Alert>>


    suspend fun setAlarm(myAlert: Alert)

    suspend fun deleteAlerts(myAlert: Alert)
}