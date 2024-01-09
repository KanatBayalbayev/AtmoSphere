package com.kanatandroider.atmosphere.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_weather")
data class CurrentWeatherDatabase(

    @PrimaryKey
    val name: String,
    val lastUpdated: String,
    val currentTempC: Double,
    val maxTempC: Double,
    val minTempC: Double,
    val windKph: Double,
    val humidity: Int,
    val feelsLikeC: Double,
    val description: String,
    val codeOfDescription: Int,
    val dailyChanceOfRain: Int,
    val sunrise: String,
    val sunset: String,
    val moonrise: String,
    val moonset: String,
    val hourlyTime: String,
    val hourlyTempC: Double,

    )
