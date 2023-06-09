package com.example.weatherapplication.utility

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapplication.R
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Utility {

    companion object{

        val TEMP_KEY = "Temp"
        val METRIC = "metric"

        fun timeStampToDate (dt: Long,lang: String) : String{
            var date : Date = Date(dt * 1000)
            var dateFormat : DateFormat = SimpleDateFormat("MMM d, yyyy",Locale(lang))
            return dateFormat.format(date)
        }
        fun timeStampToDay(dt: Long,lang: String) : String{
            var date: Date = Date(dt * 1000)
            var dateFormat : DateFormat = SimpleDateFormat("EEEE", Locale(lang))
            return dateFormat.format(date)
        }
        fun timeStampToHour(dt : Long,lang : String) : String{
            val date = Date(dt * 1000)
            date.time
            val format = SimpleDateFormat("h:mm a", Locale(lang))
            return format.format(date)
        }
         fun getDateMillis(date: String): Long {
            val f = SimpleDateFormat("dd/MM/yyyy")
            val d: Date = f.parse(date)
            return (d.time).div(1000)
        }
        fun timeStampToHourOneNumber(dt : Long) : String{
            var date: Date = Date(dt * 1000)
            var dateFormat : DateFormat = SimpleDateFormat("h")
            return dateFormat.format(date)
        }

        fun getCurrentDay(): String {
            val dateFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy")
            val date = Date()
            return dateFormat.format(date)
        }

        fun dateToLong(date: String?): Long {
            val f = SimpleDateFormat("dd-MM-yyyy")
            var milliseconds: Long = 0
            try {
                val d = f.parse(date)
                milliseconds = d.time
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return milliseconds
        }
        fun timeToMillis(hour: Int, min: Int): Long {
            return ((hour * 60 + min) * 60 * 1000).toLong()
        }

        fun saveToSharedPref(context: Context, key : String, value : Double){
            var editor : SharedPreferences.Editor = context.getSharedPreferences("LatLong",
                AppCompatActivity.MODE_PRIVATE
            ).edit()
            editor.putFloat(key, value.toFloat())
            editor.apply()
        }

        fun saveOverlayPermission(context: Context, key : String, value : Boolean){
            var editor : SharedPreferences.Editor = context.getSharedPreferences("overlay",
                AppCompatActivity.MODE_PRIVATE
            ).edit()
            editor.putBoolean(key, value)
            editor.apply()
        }

        fun saveLanguageToSharedPref(context: Context, key : String, value : String){
            var editor : SharedPreferences.Editor = context.getSharedPreferences("Language",
                AppCompatActivity.MODE_PRIVATE
            ).edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun saveTempToSharedPref(context: Context, key : String, value : String){
            var editor : SharedPreferences.Editor = context.getSharedPreferences("Units",
                AppCompatActivity.MODE_PRIVATE
            ).edit()
            editor.putString(key, value)
            editor.apply()
        }
        fun timeToSeconds(hour: Int, min: Int): Long {
            return (((hour * 60 + min) * 60) - 7200 ).toLong()
        }

        fun saveFirstTimeEnterAppSharedPref(context: Context, key : String, value : Boolean){
            var editor : SharedPreferences.Editor = context.getSharedPreferences("first",
                AppCompatActivity.MODE_PRIVATE
            ).edit()
            editor.putBoolean(key, value)
            editor.apply()
        }

        fun initSharedPref(context: Context): SharedPreferences {
            return context.getSharedPreferences(
                "sharedPref",
                Context.MODE_PRIVATE
            )

        }

        fun saveIsMapSharedPref(context: Context, key : String, value : Boolean){
            var editor : SharedPreferences.Editor = context.getSharedPreferences("isMap",
                AppCompatActivity.MODE_PRIVATE
            ).edit()
            editor.putBoolean(key, value)
            editor.apply()
        }


       fun getWeatherIcon(imageString: String): Int {
            val imageInInteger: Int
            when (imageString) {
                "01d" -> imageInInteger = R.drawable.icon_01d
                "01n" -> imageInInteger = R.drawable.icon_01n
                "02d" -> imageInInteger = R.drawable.icon_02d
                "02n" -> imageInInteger = R.drawable.icon_02n
                "03n" -> imageInInteger = R.drawable.icon_03n
                "03d" -> imageInInteger = R.drawable.icon_03d
                "04d" -> imageInInteger = R.drawable.icon_04d
                "04n" -> imageInInteger = R.drawable.icon_04n
                "09d" -> imageInInteger = R.drawable.icon_09d
                "09n" -> imageInInteger = R.drawable.icon_09n
                "10d" -> imageInInteger = R.drawable.icon_10d
                "10n" -> imageInInteger = R.drawable.icon_10n
                "11d" -> imageInInteger = R.drawable.icon_11d
                "11n" -> imageInInteger = R.drawable.icon_11n
                "13d" -> imageInInteger = R.drawable.icon_13d
                "13n" -> imageInInteger = R.drawable.icon_13n
                "50d" -> imageInInteger = R.drawable.icon_50d
                "50n" -> imageInInteger = R.drawable.icon_50n
                else -> imageInInteger = R.drawable.cloud_icon
            }
            return imageInInteger
        }

        fun convertNumbersToArabic(value: Double): String {
            return (value.toString() + "")
                .replace("1".toRegex(), "١").replace("2".toRegex(), "٢")
                .replace("3".toRegex(), "٣").replace("4".toRegex(), "٤")
                .replace("5".toRegex(), "٥").replace("6".toRegex(), "٦")
                .replace("7".toRegex(), "٧").replace("8".toRegex(), "٨")
                .replace("9".toRegex(), "٩").replace("0".toRegex(), "٠")
        }
        fun convertNumbersToArabic(value: String): String {
            return (value.toString() + "")
                .replace("1".toRegex(), "١").replace("2".toRegex(), "٢")
                .replace("3".toRegex(), "٣").replace("4".toRegex(), "٤")
                .replace("5".toRegex(), "٥").replace("6".toRegex(), "٦")
                .replace("7".toRegex(), "٧").replace("8".toRegex(), "٨")
                .replace("9".toRegex(), "٩").replace("0".toRegex(), "٠")
        }

        fun convertNumbersToArabic(value: Int): String {
            return (value.toString() + "")
                .replace("1".toRegex(), "١").replace("2".toRegex(), "٢")
                .replace("3".toRegex(), "٣").replace("4".toRegex(), "٤")
                .replace("5".toRegex(), "٥").replace("6".toRegex(), "٦")
                .replace("7".toRegex(), "٧").replace("8".toRegex(), "٨")
                .replace("9".toRegex(), "٩").replace("0".toRegex(), "٠")
        }

        fun isOnline(context: Context): Boolean {
            val cm = context?.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            return if (activeNetwork != null) {true}else{false}}

    }
}
