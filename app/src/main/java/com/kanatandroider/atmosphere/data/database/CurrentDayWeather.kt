package com.kanatandroider.atmosphere.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_weather")
data class CurrentDayWeather(
    @PrimaryKey
    val id: Int = 1,
    val name: String,
    val localtime: String,
    val lastUpdated: String,
    val currentTempC: Double,
    val windKph: Double,
    val humidity: Int,
    val feelsLikeC: Double,
    val description: String,
    val codeOfDescription: Int,
    val forecastday: String
)


