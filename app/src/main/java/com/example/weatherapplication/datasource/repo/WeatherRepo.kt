package com.example.weatherapplication.datasource.repo


import androidx.lifecycle.LiveData
import com.example.weatherapplication.datasource.database.LocalSourceInterface
import com.example.weatherapplication.datasource.network.RemoteSource
import com.example.weatherapplication.model.Alert
import com.example.weatherapplication.model.FavoriteWeather
import com.example.weatherapplication.model.OpenWeather
import retrofit2.Response


class WeatherRepo (var remoteSource: RemoteSource,var localSource:LocalSourceInterface) : WeatherRepoInterface {

    companion object {
        private var instance : WeatherRepo? = null
        fun getInstance(remoteResource: RemoteSource,localSource: LocalSourceInterface):WeatherRepo{
            return instance?: synchronized(this){
                val temp = WeatherRepo(remoteResource,localSource)
                instance = temp
                temp
            }
        }
    }



   override suspend fun getCurrentTempData(
        lat: Double, long: Double, lang: String ,tempUnit: String
    ): Response<OpenWeather>{

      return remoteSource.getCurrentTempData(lat,long,lang,tempUnit)
   }

    override suspend fun selectAllStoredWeatherModel(currentTimeZone: String): LiveData<OpenWeather> {
        return localSource.selectAllStoredWeatherModel(currentTimeZone)
    }

    override suspend fun insertWeatherModel(openWeather: OpenWeather) {
        localSource.insertWeatherModel(openWeather)
    }

    override suspend fun getAllFavoriteWeather(): LiveData<List<FavoriteWeather>> {
        return localSource.getAllFavoriteWeather()
    }

    override suspend fun deleteFavoritePlace(favoriteWeather: FavoriteWeather) {
        localSource.deleteFavoritePlace(favoriteWeather)
    }

    override suspend fun insertFavoritePlace(favoriteWeather: FavoriteWeather) {
        localSource.insertFavoritePlace(favoriteWeather)
    }

    override suspend fun updateFavoritePlace(favoriteWeather: FavoriteWeather) {
        localSource.updateFavoritePlace(favoriteWeather)
    }

    override suspend fun getFavWeatherData(favWeather: FavoriteWeather): Response<OpenWeather> {
        return remoteSource.getFavWeatherData(favWeather)
    }

    override suspend fun getAlerts(): LiveData<List<Alert>> {
        return localSource.getAlerts()
    }

    override suspend fun insertAlert(myAlert: Alert) {
        localSource.insertAlert(myAlert)
    }

    override suspend fun deleteAlerts(myAlert: Alert) {
        localSource.deleteAlerts(myAlert)
    }


}