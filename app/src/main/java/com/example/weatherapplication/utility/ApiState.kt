package com.example.weatherapplication.utility

import com.example.weatherapplication.model.Alert
import com.example.weatherapplication.model.FavoriteWeather
import com.example.weatherapplication.model.OpenWeather

sealed class ApiState {
    class Success(var data: OpenWeather) : ApiState()
    class Failure(val msg: Throwable) : ApiState()
    object Loading : ApiState()
}

sealed class ApiStateList {
    class Success(var data: List<FavoriteWeather>) : ApiStateList()
    class Failure(val msg: Throwable) : ApiStateList()
    object Loading : ApiStateList()
}

sealed class ApiStateAlert {

    class Success(var data: List<Alert>) : ApiStateAlert()
    class Failure(val msg: Throwable) : ApiStateAlert()
    object Loading : ApiStateAlert()
}