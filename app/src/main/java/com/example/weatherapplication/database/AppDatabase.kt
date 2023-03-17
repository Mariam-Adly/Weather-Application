package com.example.weatherapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapplication.model.Weather

@Database(entities = arrayOf(Weather::class), version = 1 )
abstract class AppDatabase : RoomDatabase() {
    abstract fun getWeatherDao(): WeatherDao
    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance (ctx: Context): AppDatabase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext, AppDatabase::class.java, "weather_database")
                    .build()
                INSTANCE = instance
// return instance
                instance }
        }
    }
}
