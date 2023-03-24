package com.example.weatherapplication.utility


import androidx.room.TypeConverter
import com.example.weatherapplication.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class Converters {
//
//    @TypeConverter
//    fun fromHourlyToString(hourly: List<Hourly>): String {
//        return Gson().toJson(hourly)
//    }
//
//    @TypeConverter
//    fun fromDailyToString(daily: List<Daily>): String {
//        return Gson().toJson(daily)
//    }
//
//    @TypeConverter
//    fun fromCurrentToString(current: Current): String {
//        return Gson().toJson(current)
//    }
//
//    @TypeConverter
//    fun fromStringToCurrent(current: String): Current {
//        return Gson().fromJson(current, Current::class.java)
//    }
//
//    @TypeConverter
//    fun fromAlertsToString(alerts: List<Alerts>?): String {
//        if (!alerts.isNullOrEmpty()) {
//            return Gson().toJson(alerts)
//        }
//        return ""
//    }
//
//
//    @TypeConverter
//    fun fromStringToHourly(hourly: String): List<Hourly> {
//        val listType: Type = object : TypeToken<List<Hourly?>?>() {}.type
//        return Gson().fromJson(hourly, listType)
//    }
//
//    @TypeConverter
//    fun fromStringToDaily(daily: String): List<Daily> {
//        val listType = object : TypeToken<List<Daily?>?>() {}.type
//        return Gson().fromJson(daily, listType)
//    }
//
//    @TypeConverter
//    fun fromStringToAlerts(alerts: String?): List<Alerts> {
//        if (alerts.isNullOrEmpty()) {
//            return emptyList()
//        }
//        val listType = object : TypeToken<List<Alerts?>?>() {}.type
//        return Gson().fromJson(alerts, listType)
//    }


//    @TypeConverter
//    fun fromWeatherToString(weather: Weather): String {
//        return Gson().toJson(weather)
//    }
//
//    @TypeConverter
//    fun fromStringToWeather(weather: String): Weather {
//        val listType = object : TypeToken<List<Weather?>?>() {}.type
//        return Gson().fromJson(weather, listType)
//    }

    @TypeConverter
    fun currentToJson(current: Current?) = Gson().toJson(current)

    @TypeConverter
    fun jsonToCurrent(currentString: String) =
        Gson().fromJson(currentString, Current::class.java)

    @TypeConverter
    fun hourlyListToJson(hourlyList: List<Hourly>?) = Gson().toJson(hourlyList)

    @TypeConverter
    fun jsonToHourlyList(hourlyString: String) =
        Gson().fromJson(hourlyString, Array<Hourly>::class.java)?.toList()

    @TypeConverter
    fun dailyListToJson(dailyList: List<Daily>) = Gson().toJson(dailyList)

    @TypeConverter
    fun jsonToDailyList(dailyString: String) =
        Gson().fromJson(dailyString, Array<Daily>::class.java).toList()

    @TypeConverter
    fun weatherListToJson(weatherList: List<Weather>) = Gson().toJson(weatherList)

    @TypeConverter
    fun jsonToWeatherList(weatherString: String) =
        Gson().fromJson(weatherString, Array<Weather>::class.java).toList()

    @TypeConverter
    fun alertListToJson(alertList: List<Alerts>?) = Gson().toJson(alertList)

    @TypeConverter
    fun jsonToAlertList(alertString: String?): List<Alerts>? {
        alertString?.let {
            return Gson().fromJson(alertString, Array<Alerts>::class.java)?.toList()
        }
        return emptyList()
    }

}
