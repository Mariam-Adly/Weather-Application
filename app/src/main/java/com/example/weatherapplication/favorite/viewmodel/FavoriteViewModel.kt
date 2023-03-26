package com.example.weatherapplication.favorite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.datasource.repo.WeatherRepoInterface
import com.example.weatherapplication.model.FavoriteWeather
import com.example.weatherapplication.model.OpenWeather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(val repo : WeatherRepoInterface) : ViewModel() {

    private val _mutableFavWeather = MutableLiveData<List<OpenWeather>>()
    var favWeather : LiveData<List<OpenWeather>> = _mutableFavWeather

    fun getFavPlaces():LiveData<List<FavoriteWeather>>{
        return repo.getAllFavoriteWeather
    }

    fun deleteFavoritePlace(favoriteWeather: FavoriteWeather) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteFavoritePlace(favoriteWeather)
        }
    }

    fun insertFavoritePlace(favoriteWeather: FavoriteWeather) {
        viewModelScope.launch(Dispatchers.IO ) {
            repo.insertFavoritePlace(favoriteWeather)
        }
    }


}