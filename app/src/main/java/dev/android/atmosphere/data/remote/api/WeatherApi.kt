package dev.android.atmosphere.data.remote.api

import dev.android.atmosphere.data.remote.dto.CurrentWeatherDto
import dev.android.atmosphere.data.remote.dto.ForecastDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("current.json")
    suspend fun getCurrentWeatherByCoordinates(
        @Query("q") coordinates: String,
        @Query("key") apiKey: String = API_KEY
    ): CurrentWeatherDto

    @GET("current.json")
    suspend fun getCurrentWeatherByCity(
        @Query("q") cityName: String,
        @Query("key") apiKey: String = API_KEY
    ): CurrentWeatherDto

    @GET("forecast.json")
    suspend fun getForecastByCoordinates(
        @Query("q") coordinates: String,
        @Query("days") days: Int = 5,
        @Query("key") apiKey: String = API_KEY
    ): ForecastDto

    @GET("forecast.json")
    suspend fun getForecastByCity(
        @Query("q") cityName: String,
        @Query("days") days: Int = 5,
        @Query("key") apiKey: String = API_KEY
    ): ForecastDto


    companion object {
        private const val API_KEY = "7ea73ae0e63642eb87e73049250504"
        const val BASE_URL = "https://api.weatherapi.com/v1/"
    }
}