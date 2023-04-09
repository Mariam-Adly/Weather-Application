package com.example.weatherapplication.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.datasource.repo.WeatherRepoInterface
import com.example.weatherapplication.utility.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class HomeViewModel(val repo : WeatherRepoInterface) : ViewModel(){

    private val _mutableWeather = MutableStateFlow<ApiState>(ApiState.Loading)
    var weather : MutableStateFlow<ApiState> = _mutableWeather

    fun getCurrentTemp(
    lat: Double,
    long: Double,
    lang: String,
    tempUnit:String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getCurrentTempData(lat,long,lang,tempUnit).catch{
                e-> _mutableWeather.value = ApiState.Failure(e)
            }
                .collectLatest {
                    _mutableWeather.value = ApiState.Success(it.body()!!)
                    repo.insertWeatherModel(it.body()!!)
                }
            weather = _mutableWeather
        }

    }


    fun getCurrentWeatherDB() : StateFlow<ApiState> {
        viewModelScope.launch(Dispatchers.IO){
            repo.selectAllStoredWeatherModel().catch { e ->
                _mutableWeather.value = ApiState.Failure(e)
            }
                .collectLatest {
                    _mutableWeather.value = ApiState.Success(it)
                }
        }
        weather = _mutableWeather
        return weather
    }

}