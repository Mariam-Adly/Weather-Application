package com.example.weatherapplication.favorite.view

import com.example.weatherapplication.model.FavoriteWeather

interface OnClickFavPlaceListener {
    fun onClickFavPlace(favPlace: FavoriteWeather)
    fun deleteTask(myFav: FavoriteWeather, position: Int)
}