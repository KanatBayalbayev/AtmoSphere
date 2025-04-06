package dev.android.atmosphere.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import dev.android.atmosphere.data.util.DateConverter
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "daily_forecast")
@TypeConverters(DateConverter::class)
data class DailyForecastEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cityName: String,
    val date: LocalDate,
    val maxTemp: Double,
    val minTemp: Double,
    val avgTemp: Double,
    val maxWind: Double,
    val totalPrecipitation: Double,
    val avgHumidity: Double,
    val chanceOfRain: Int,
    val chanceOfSnow: Int,
    val conditionText: String,
    val conditionIcon: String,
    val conditionCode: Int,
    val sunrise: String,
    val sunset: String,
    val moonPhase: String
)

@Entity(tableName = "hourly_forecast")
@TypeConverters(DateConverter::class)
data class HourlyForecastEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cityName: String,
    val forecastDate: LocalDate,
    val time: LocalDateTime,
    val temperature: Double,
    val feelsLike: Double,
    val conditionText: String,
    val conditionIcon: String,
    val conditionCode: Int,
    val windSpeed: Double,
    val windDirection: String,
    val pressure: Int,
    val precipitation: Double,
    val humidity: Int,
    val cloudCover: Int,
    val chanceOfRain: Int,
    val isDay: Boolean
)