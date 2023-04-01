package com.example.weatherapplication.model

import androidx.room.Entity

@Entity(primaryKeys = ["startDay","endDay","Time"])
data class Alert(
    var Time: Long,
    var startDay: Long,
    var endDay: Long,
    var lat: Double,
    var lon: Double,
    var AlertCityName :String
)