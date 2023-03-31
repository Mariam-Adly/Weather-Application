package com.example.weatherapplication.favorite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.datasource.repo.WeatherRepoInterface
import com.example.weatherapplication.model.FavoriteWeather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(val repo : WeatherRepoInterface) : ViewModel() {

    private val _mutableFavWeather = MutableLiveData<List<FavoriteWeather>>()
    var favWeather : LiveData<List<FavoriteWeather>> = _mutableFavWeather

    fun getFavPlaces():LiveData<List<FavoriteWeather>>{
        viewModelScope.launch(Dispatchers.IO) {


            favWeather = repo.getAllFavoriteWeather()
        }
        return favWeather
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