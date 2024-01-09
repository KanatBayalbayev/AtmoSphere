package com.kanatandroider.atmosphere.data.api.network

import com.kanatandroider.atmosphere.data.api.models.WeatherDataDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {


    @GET("forecast.json")
    suspend fun getData(
        @Query(QUERY_API_KEY) api_key: String = API_KEY,
        @Query(QUERY_CITY) city: String = "Almaty",
        @Query(QUERY_DAYS) days: String = DAYS,
        @Query(QUERY_AQI) aqi: String = "no",
        @Query(QUERY_ALERTS) alerts: String = "no",
        @Query(QUERY_LANG) lang: String = "ru",
    ): WeatherDataDTO

    private companion object {
        const val QUERY_API_KEY = "key"
        const val QUERY_CITY = "q"
        const val QUERY_DAYS = "days"
        const val QUERY_AQI = "aqi"
        const val QUERY_ALERTS = "alerts"
        const val QUERY_LANG = "lang"

        const val API_KEY = "dd2c1ef1a2c44dc2a6733324231109"
        const val DAYS = "3"

    }

}