package com.kanatandroider.atmosphere.domain.models


import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class HourEntity(
    val time: String,
    val tempC: Double,
    val windKph: Double,
    val humidity: Int,
    val feelslikeC: Double,
    val chanceOfRain: Int,
)