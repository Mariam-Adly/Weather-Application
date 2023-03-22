package com.example.weatherapplication.model.models

import com.example.weatherapplication.model.OpenWeather
import com.example.weatherapplication.model.network.RemoteSource
import retrofit2.Response



const val defaultLat : Double = 31.205753
const val defaultlong =  29.924526
const val defaultlang = ""
const val defaulttempUnit = ""

class WeatherRepo (var remoteSource: RemoteSource) : WeatherRepoInterface {

    companion object {
        private var instance : WeatherRepo? = null
        fun getInstance(remoteResource: RemoteSource):WeatherRepo{
            return instance?: synchronized(this){
                val temp = WeatherRepo(remoteResource)
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




}