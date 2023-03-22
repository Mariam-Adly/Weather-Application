package com.example.weatherapplication.model.network

import com.example.weatherapplication.model.OpenWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

private const val appId ="4a059725f93489b95183bbcb8c6829b9"
private const val excludeMinutes = "minutely"
    //"4a059725f93489b95183bbcb8c6829b9"
    //"67a004872bca4e7a1a7edbed26715b28"

interface ApiService {

    @GET("onecall")
    suspend fun getCurrentTempData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String ,
        @Query("lang") lang: String = "eng",
        @Query("exclude") exclude: String = excludeMinutes,
        @Query("appid") appid: String = appId
    ): Response<OpenWeather>

}