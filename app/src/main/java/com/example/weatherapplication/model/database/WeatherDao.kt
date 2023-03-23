package com.example.weatherapplication.model.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherapplication.model.OpenWeather
@Dao
interface WeatherDao {

    @get:Query("SELECT * FROM weather WHERE id = 'id'")
    val getCurrentWeather : LiveData<OpenWeather>

    @Query("SELECT * FROM weather WHERE id = 'id'")
    suspend fun getStoredWeather() : OpenWeather

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrentWeather(weather : OpenWeather)


//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertWeather(openWeatherJason: OpenWeather):Long
//
//    // for home screen not fav
//    @Query("Select * from weather where  id =:id And isFavourite  =:isFavourite ")
//    suspend fun getCurrentWeatherZone(id: Int, isFavourite: Boolean): OpenWeather
//
//    @Query("Select * from weather where  lat =:lat AND lon =:lon")
//    suspend fun getCurrentWeather(lat: Double, lon: Double): OpenWeather
//    // for fav
//
//    @Update()
//    suspend fun updateWeather(openWeatherJason: OpenWeather)
//
//    @Delete()
//    suspend fun deleteWeatherTimeZone(openWeatherJason: OpenWeather)
//    // ensure exist or not
//
//    //get Favourites Weathers
//    @Query("Select * from weather where   isFavourite  =1 ")
//    suspend fun getFavWeathersZone(): List<OpenWeather>
//
//    // delete Favourite Weather
//    @Query("Delete from weather where  id =:id  And isFavourite=1 ")
//    suspend fun deleteFavWeather(id: Int)
//
//    // for Alerts
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertWeatherAlert(alert: WeatherAlert):Long
//
//    @Query("Select * from Alert ")
//    fun getWeatherAlerts(): Flow<List<WeatherAlert>>
//
//    @Query("Delete from Alert where id=:id")
//    suspend fun deleteWeatherAlert(id: Int)
//    @Query("Select * from Alert where id=:id")
//    suspend fun getWeatherAlert(id:Int):WeatherAlert
}