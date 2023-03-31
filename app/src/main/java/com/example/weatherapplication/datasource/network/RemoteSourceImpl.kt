package com.example.weatherapplication.datasource.network

import com.example.weatherapplication.model.FavoriteWeather
import com.example.weatherapplication.model.OpenWeather
import retrofit2.Response

class RemoteSourceImpl : RemoteSource {

    val apiService : ApiService by lazy {
        RetrofitHelper.getInstance().create(ApiService::class.java)
    }

    companion object {
        private var instance : RemoteSourceImpl? = null
        fun getInstance(): RemoteSourceImpl {
            return instance ?: synchronized(this){
                val temp = RemoteSourceImpl()
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
        return apiService.getFavWeatherData(favoriteWeather.lat,favoriteWeather.lon,"metric","eng","minutely","67a004872bca4e7a1a7edbed26715b28")
    }


}