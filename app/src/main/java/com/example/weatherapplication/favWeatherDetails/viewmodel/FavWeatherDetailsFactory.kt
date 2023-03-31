package com.example.weatherapplication.favWeatherDetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapplication.datasource.repo.WeatherRepoInterface

class FavWeatherDetailsFactory(private val repo : WeatherRepoInterface) : ViewModelProvider.Factory{


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(FavWeatherDetailsViewModel::class.java)){
            FavWeatherDetailsViewModel(repo) as T
        }else{
            throw IllegalArgumentException("view model class not found")
        }
    }
}