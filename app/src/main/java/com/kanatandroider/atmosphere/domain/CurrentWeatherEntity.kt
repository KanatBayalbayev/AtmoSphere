package com.kanatandroider.atmosphere.domain

import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.kanatandroider.atmosphere.domain.models.ForcastDayEntity

data class CurrentWeatherEntity(
    val name: String,
    val lastUpdated: String,
    val currentTempC: Double,
//    val maxTempC: Double,
//    val minTempC: Double,
    val windKph: Double,
    val humidity: Int,
    val feelsLikeC: Double,
    val description: String,
    val codeOfDescription: Int,
    val days: List<ForcastDayEntity>
//    val dailyChanceOfRain: Int,
//    val sunrise: String,
//    val sunset: String,
//    val moonrise: String,
//    val moonset: String,
//    val hourlyTime: String,
//    val hourlyTempC: Double,
)
