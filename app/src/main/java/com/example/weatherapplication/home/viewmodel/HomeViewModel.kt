package com.example.weatherapplication.home.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.model.OpenWeather
import com.example.weatherapplication.datasource.repo.WeatherRepoInterface
import com.example.weatherapplication.utility.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
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
                    Log.d("mariam", "getCurrentTemp: success ${response.body()}")
                }
            }else{
                Log.d("mariam", "getCurrentTemp: error ${response.errorBody()}")
            }

        }

    }

    private var _data: MutableStateFlow<ApiState>
    var data: StateFlow<ApiState>


    init {
        _data = MutableStateFlow(ApiState.Loading)
        data= _data
    }

    fun getCurrentWeatherDB() : StateFlow<ApiState> {
        viewModelScope.launch(Dispatchers.IO){
            repo.selectAllStoredWeatherModel().catch { e ->
                _data.value = ApiState.Failure(e)
            }
                .collectLatest {
                    _data.value = ApiState.Success(it)
                }
        }
        data = _data
        return data
    }

}