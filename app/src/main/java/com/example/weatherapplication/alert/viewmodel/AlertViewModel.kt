package com.example.weatherapplication.alert.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.datasource.repo.WeatherRepoInterface
import com.example.weatherapplication.model.Alert
import com.example.weatherapplication.utility.ApiState
import com.example.weatherapplication.utility.ApiStateAlert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.*


class AlertViewModel (val repo : WeatherRepoInterface) : ViewModel(){

    private val _mutableAlertWeather = MutableStateFlow<ApiStateAlert>(ApiStateAlert.Loading)
    var alertWeather : StateFlow<ApiStateAlert> = _mutableAlertWeather

    fun getAlert(): StateFlow<ApiStateAlert> {
        viewModelScope.launch(Dispatchers.IO) {
           repo.getAlerts().catch{
                e-> _mutableAlertWeather.value = ApiStateAlert.Failure(e)
            }
               .collectLatest {
                   _mutableAlertWeather.value = ApiStateAlert.Success(it)
               }
        }
        alertWeather = _mutableAlertWeather
       return alertWeather
    }

    fun deleteAlert(myAlert: Alert) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAlerts(myAlert)
        }
    }

    fun setAlarm(myAlert: Alert) {
        viewModelScope.launch(Dispatchers.IO ) {
            repo.setAlarm(myAlert)
        }
    }



}