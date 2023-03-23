package com.example.weatherapplication.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


//@Database(entities = [OpenWeather::class, WeatherAlert::class], version = 1 )
//@TypeConverters(Converters::class)
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
