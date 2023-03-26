package com.example.weatherapplication.datasource.repo


import androidx.lifecycle.LiveData
import com.example.weatherapplication.datasource.database.LocalSourceInterface
import com.example.weatherapplication.datasource.network.RemoteSource
import com.example.weatherapplication.model.FavoriteWeather
import com.example.weatherapplication.model.OpenWeather
import retrofit2.Response



const val defaultLat : Double = 31.205753
const val defaultlong =  29.924526
const val defaultlang = ""
const val defaulttempUnit = ""

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
      // val language = sharedPreferences.getLanguage()

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


}