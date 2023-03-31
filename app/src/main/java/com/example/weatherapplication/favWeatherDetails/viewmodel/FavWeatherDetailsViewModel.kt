package com.example.weatherapplication.favWeatherDetails.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.datasource.repo.WeatherRepoInterface
import com.example.weatherapplication.model.FavoriteWeather
import com.example.weatherapplication.model.OpenWeather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavWeatherDetailsViewModel(val repo : WeatherRepoInterface) : ViewModel(){

    private val _mutableWeather = MutableLiveData<OpenWeather>()
    var weather : LiveData<OpenWeather> = _mutableWeather

    fun getFavWeatherData(favoriteWeather: FavoriteWeather): LiveData<OpenWeather>{
        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getFavWeatherData(favoriteWeather)
            withContext(Dispatchers.Main) {
                _mutableWeather.postValue(response.body())
                Log.i("mariam", "getFavWeatherData: $response")
            }
        }
      return weather
    }
}