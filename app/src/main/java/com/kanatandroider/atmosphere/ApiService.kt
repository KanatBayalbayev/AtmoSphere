package com.kanatandroider.atmosphere

import com.kanatandroider.atmosphere.models.WeatherData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {


    @GET("forecast?")
    suspend fun getData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") apiKey: String = APIKEY
    ): WeatherData

    @GET("forecast?")
    suspend fun getDataByCity(
        @Query("q") city: String = "Almaty",
        @Query("appid") apiKey: String = APIKEY
    ): WeatherData


    companion object {
        const val API_KEY = "appid"
        const val APIKEY = "520173c51a58fa1ce4c277f097f932d0"
    }
}