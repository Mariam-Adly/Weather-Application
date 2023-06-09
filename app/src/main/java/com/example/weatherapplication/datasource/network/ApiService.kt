package com.example.weatherapplication.datasource.network

import com.example.weatherapplication.model.OpenWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

private const val appId ="67a004872bca4e7a1a7edbed26715b28"
private const val excludeMinutes = "minutely"


interface ApiService {

    @GET("onecall")
    suspend fun getCurrentTempData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String,
        @Query("lang") lang: String = "eng",
        @Query("exclude") exclude: String = excludeMinutes,
        @Query("appid") appid: String = appId
    ): Response<OpenWeather>

    @GET("onecall")
      suspend fun getFavWeatherData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String,
        @Query("lang") lang: String = "eng",
        @Query("exclude") exclude: String = excludeMinutes,
        @Query("appid") appid: String = appId
         ):Response<OpenWeather>

}