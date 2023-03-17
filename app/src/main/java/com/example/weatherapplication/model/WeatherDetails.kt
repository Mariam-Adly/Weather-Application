package com.example.weatherapplication.model

data class WeatherDetails(
    val id: Long,
    val main: String,
    val description: String,
    val icon: String
)