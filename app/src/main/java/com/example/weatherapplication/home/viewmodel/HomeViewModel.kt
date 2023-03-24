package com.example.weatherapplication.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.model.OpenWeather
import com.example.weatherapplication.datasource.repo.WeatherRepoInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeViewModel(val repo : WeatherRepoInterface) : ViewModel(){



    private val _mutableWeather = MutableLiveData<OpenWeather>()
    var weather : LiveData<OpenWeather> = _mutableWeather

    fun getCurrentTemp(
    lat: Double,
    long: Double,
    lang: String,
    tempUnit:String) {
        viewModelScope.launch(Dispatchers.IO) {
           val response= repo.getCurrentTempData(lat,long,lang,tempUnit)
            if (response.isSuccessful){
                withContext(Dispatchers.Main){
                    _mutableWeather.value = response.body()
                    Log.d("jessie", "getCurrentTemp: success ${response.body()}")
                }
            }else{
                Log.d("jessie", "getCurrentTemp: error ${response.errorBody()}")
            }

        }

    }

}