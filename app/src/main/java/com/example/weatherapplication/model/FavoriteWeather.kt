package com.example.weatherapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.io.Serializable

@Entity(tableName = "favoriteWeather")
data class FavoriteWeather(

    var favLocationName : String,
    var lat : Double,
    var lon : Double
): Serializable {
    @NotNull
    @PrimaryKey(autoGenerate = true)
    var favId : Int = 0
}