package com.example.weatherapplication.favorite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.datasource.repo.WeatherRepoInterface
import com.example.weatherapplication.model.FavoriteWeather
import com.example.weatherapplication.model.OpenWeather
import com.example.weatherapplication.utility.ApiStateList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FavoriteViewModel(val repo : WeatherRepoInterface) : ViewModel() {

    private val _mutableFavWeather = MutableStateFlow<ApiStateList>(ApiStateList.Loading)
    var favWeather : StateFlow<ApiStateList> = _mutableFavWeather

    fun getFavPlaces(): StateFlow<ApiStateList> {
        viewModelScope.launch(Dispatchers.IO) {
           repo.getAllFavoriteWeather().catch {
               e-> _mutableFavWeather.value = ApiStateList.Failure(e)
           }
               .collectLatest {
                   _mutableFavWeather.value=ApiStateList.Success(it)
               }
        }
        favWeather = _mutableFavWeather
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