package com.example.weatherapplication.alert.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapplication.datasource.repo.WeatherRepoInterface
import com.example.weatherapplication.favWeatherDetails.viewmodel.FavWeatherDetailsViewModel

class AlertViewModelFactory (private val repo : WeatherRepoInterface) : ViewModelProvider.Factory{


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(AlertViewModel::class.java)){
            AlertViewModel(repo) as T
        }else{
            throw IllegalArgumentException("view model class not found")
        }
    }
}