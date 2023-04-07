package com.example.weatherapplication.alert.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.datasource.repo.WeatherRepoInterface
import com.example.weatherapplication.model.Alert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AlertViewModel (val repo : WeatherRepoInterface) : ViewModel(){

    private val _mutableAlertWeather = MutableLiveData<List<Alert>>()
    var alertWeather : LiveData<List<Alert>> = _mutableAlertWeather

    fun getAlert(): LiveData<List<Alert>> {
        viewModelScope.launch(Dispatchers.IO) {
            alertWeather = repo.getAlerts()
        }
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