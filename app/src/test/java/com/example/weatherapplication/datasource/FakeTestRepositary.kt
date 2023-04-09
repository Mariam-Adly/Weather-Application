package com.example.weatherforecast.DataSource

import com.example.weatherapplication.datasource.database.LocalSourceInterface
import com.example.weatherapplication.datasource.network.RemoteSource
import com.example.weatherapplication.datasource.repo.WeatherRepoInterface
import com.example.weatherapplication.model.Alert
import com.example.weatherapplication.model.FavoriteWeather
import com.example.weatherapplication.model.OpenWeather
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class FakeTestRepositary(private var localDataSource: LocalSourceInterface,private var remoteDataSource: RemoteSource): WeatherRepoInterface {


    override suspend fun getCurrentTempData(
        lat: Double,
        long: Double,
        lang: String,
        tempUnit: String
    ): Flow<Response<OpenWeather>> {
        TODO("Not yet implemented")
    }

    override suspend fun selectAllStoredWeatherModel(): Flow<OpenWeather> {
        return localDataSource.selectAllStoredWeatherModel()
    }

    override suspend fun insertWeatherModel(openWeather: OpenWeather) {
        localDataSource.insertWeatherModel(openWeather)
    }

    override suspend fun getCurrentWeatherForAlert(
        lat: Double,
        lon: Double
    ): Response<OpenWeather> {
        return remoteDataSource.getCurrentTempData(lat,lon,"eng","metric")
    }

    override suspend fun getAllFavoriteWeather(): Flow<List<FavoriteWeather>> {
        return localDataSource.getAllFavoriteWeather()
    }

    override suspend fun deleteFavoritePlace(favoriteWeather: FavoriteWeather) {
       return localDataSource.deleteFavoritePlace(favoriteWeather)
    }

    override suspend fun insertFavoritePlace(favoriteWeather: FavoriteWeather) {
        return localDataSource.insertFavoritePlace(favoriteWeather)
    }

    override suspend fun updateFavoritePlace(favoriteWeather: FavoriteWeather) {
        TODO("Not yet implemented")
    }

    override suspend fun getFavWeatherData(favWeather: FavoriteWeather): Response<OpenWeather> {
        return remoteDataSource.getFavWeatherData(favWeather)
    }

    override suspend fun getAlerts(): Flow<List<Alert>> {
        return localDataSource.getAlerts()
    }

    override suspend fun setAlarm(myAlert: Alert) {
       return localDataSource.insertAlert(myAlert)
    }

    override suspend fun deleteAlerts(myAlert: Alert) {
        return localDataSource.deleteAlerts(myAlert)
    }
}