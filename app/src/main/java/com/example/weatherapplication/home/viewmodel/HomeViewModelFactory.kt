package com.example.weatherapplication.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapplication.model.models.WeatherRepoInterface

class HomeViewModelFactory (private val repo : WeatherRepoInterface) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            HomeViewModel(repo) as T
        }else{
            throw IllegalArgumentException("view model class not found")
        }
    }
}