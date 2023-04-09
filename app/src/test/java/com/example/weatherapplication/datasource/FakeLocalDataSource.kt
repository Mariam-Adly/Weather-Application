package com.example.weatherapplication.datasource

import com.example.weatherapplication.datasource.database.LocalSourceInterface
import com.example.weatherapplication.model.Alert
import com.example.weatherapplication.model.FavoriteWeather
import com.example.weatherapplication.model.OpenWeather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Response


class FakeLocalDataSource(private var alertList: MutableList<Alert> = mutableListOf(),
                          private var openWeatherList: MutableList<OpenWeather> = mutableListOf() , private var current: Response<OpenWeather> , private var favoriteWeatherList: MutableList<FavoriteWeather> = mutableListOf()
): LocalSourceInterface {


    override suspend fun getAllFavoriteWeather(): Flow<List<FavoriteWeather>> {
        return flowOf(favoriteWeatherList)
    }

    override suspend fun selectAllStoredWeatherModel(): Flow<OpenWeather> {
        TODO("Not yet implemented")
    }

    override suspend fun insertWeatherModel(openWeather: OpenWeather) {
        openWeatherList.add(openWeather)
    }

    override suspend fun deleteFavoritePlace(favoriteWeather: FavoriteWeather) {
        favoriteWeatherList.remove(favoriteWeather)
    }

    override suspend fun insertFavoritePlace(favoriteWeather: FavoriteWeather) {
        favoriteWeatherList.add(favoriteWeather)
    }

    override suspend fun updateFavoritePlace(favoriteWeather: FavoriteWeather) {
        TODO("Not yet implemented")
    }

    override suspend fun getAlerts(): Flow<List<Alert>> {
        return flowOf(alertList)
    }

    override suspend fun insertAlert(myAlert: Alert) {
        alertList.add(myAlert)
    }


    override suspend fun deleteAlerts(myAlert: Alert) {
       alertList.remove(myAlert)
    }


}