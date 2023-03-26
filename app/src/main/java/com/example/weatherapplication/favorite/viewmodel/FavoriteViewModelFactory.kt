package com.example.weatherapplication.favorite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapplication.datasource.repo.WeatherRepoInterface
import com.example.weatherapplication.home.viewmodel.HomeViewModel

class FavoriteViewModelFactory (private val repo : WeatherRepoInterface) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(FavoriteViewModel::class.java)){
            FavoriteViewModel(repo) as T
        }else{
            throw IllegalArgumentException("view model class not found")
        }
    }
}