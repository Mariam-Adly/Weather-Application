package com.example.weatherapplication.model.sharedprefernces

import android.content.Context

class WeatherSharedPreferences (val context: Context) {

    companion object {
        const val SharedPreferencesName = "my_preferences"
        const val LANGUAGE_KEY = "LANGUAGE_KEY"
    }

    val sharedPref = context.getSharedPreferences( SharedPreferencesName, Context.MODE_PRIVATE)
    val editor = sharedPref.edit()

    fun setLanguage(lang: String) {
        editor.putString(LANGUAGE_KEY, lang).apply()
    }

    fun getLanguage(): String {
       val language = sharedPref.getString(LANGUAGE_KEY, null)
        return language ?: "eng"
    }


}