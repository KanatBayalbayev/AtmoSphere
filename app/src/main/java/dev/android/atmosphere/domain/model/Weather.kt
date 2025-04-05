package dev.android.atmosphere.domain.model

import java.time.LocalDateTime

data class Weather(
    val id: Long = 0,
    val cityName: String,
    val region: String,
    val country: String,
    val temperature: Double,
    val feelsLike: Double,
    val humidity: Int,
    val pressure: Int,
    val windSpeed: Double,
    val windDirection: Int,
    val windDirectionText: String,
    val description: String,
    val icon: String,
    val uv: Double,
    val cloudCover: Int,
    val precipitation: Double,
    val visibility: Double,
    val isDay: Boolean,
    val timestamp: LocalDateTime,
    val lastUpdated: LocalDateTime
)
