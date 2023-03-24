package com.example.weatherapplication.datasource.network

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
//        var sharedPreferences : SharedPreferences = context.getSharedPreferences(Utility.latLongSharedPrefKey, AppCompatActivity.MODE_PRIVATE)
//        var languageShared : SharedPreferences = context.getSharedPreferences("Language", AppCompatActivity.MODE_PRIVATE)
//        var unitsShared : SharedPreferences = context.getSharedPreferences("Units", AppCompatActivity.MODE_PRIVATE)
       return apiService.getCurrentTempData(lat,long,tempUnit,lang)

    }
}