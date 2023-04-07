package com.example.weatherapplication.datasource.network

import android.app.Activity
import android.content.Context
import com.example.weatherapplication.model.FavoriteWeather
import com.example.weatherapplication.model.OpenWeather
import retrofit2.Response

class RemoteSourceImpl(var context: Context) : RemoteSource {

    val apiService : ApiService by lazy {
        RetrofitHelper.getInstance().create(ApiService::class.java)
    }


    val sharedPreferences = context.getSharedPreferences("language", Activity.MODE_PRIVATE)
     var lang = sharedPreferences.getString("myLang","")!!

    companion object {
        private var instance : RemoteSourceImpl? = null
        fun getInstance(context: Context): RemoteSourceImpl{
            return instance ?: synchronized(this){
                val temp = RemoteSourceImpl(context)
                instance = temp
                temp
            }
        }
    }

    override suspend fun getCurrentTempData(
        lat: Double,
        long: Double,
        lang: String,
        tempUnit: String
    ): Response<OpenWeather> {
       return apiService.getCurrentTempData(lat,long,tempUnit,lang)

    }

    override suspend fun getFavWeatherData(favoriteWeather: FavoriteWeather): Response<OpenWeather> {
        return apiService.getFavWeatherData(favoriteWeather.lat,favoriteWeather.lon,"metric",lang,"minutely","67a004872bca4e7a1a7edbed26715b28")
    }


}