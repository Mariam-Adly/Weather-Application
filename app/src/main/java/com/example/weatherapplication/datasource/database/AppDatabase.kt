package com.example.weatherapplication.datasource.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapplication.model.FavoriteWeather
import com.example.weatherapplication.model.OpenWeather
import com.example.weatherapplication.model.WeatherAlert
import com.example.weatherapplication.utility.Converters


@Database(entities = [OpenWeather::class, WeatherAlert::class, FavoriteWeather::class], version = 2,exportSchema = false )
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getWeatherDao(): WeatherDao
    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance (ctx: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext,
                    AppDatabase::class.java,
                    "weather_database")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance }
        }
    }
}
