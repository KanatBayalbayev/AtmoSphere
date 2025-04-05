package dev.android.atmosphere.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

data class Forecast(
    val cityName: String,
    val region: String,
    val country: String,
    val currentWeather: Weather?,
    val dailyForecasts: List<DailyForecast>,
    val hourlyForecasts: List<HourlyForecast>
)

data class DailyForecast(
    val date: LocalDate,
    val maxTemp: Double,
    val minTemp: Double,
    val avgTemp: Double,
    val maxWind: Double,
    val totalPrecipitation: Double,
    val avgHumidity: Double,
    val chanceOfRain: Int,
    val chanceOfSnow: Int,
    val condition: WeatherCondition,
    val sunrise: String,
    val sunset: String,
    val moonPhase: String
)

data class HourlyForecast(
    val time: LocalDateTime,
    val temperature: Double,
    val feelsLike: Double,
    val condition: WeatherCondition,
    val windSpeed: Double,
    val windDirection: String,
    val pressure: Int,
    val precipitation: Double,
    val humidity: Int,
    val cloudCover: Int,
    val chanceOfRain: Int,
    val isDay: Boolean
)

data class WeatherCondition(
    val text: String,
    val icon: String,
    val code: Int
)