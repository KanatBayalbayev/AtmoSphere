package com.kanatandroider.atmosphere

import com.kanatandroider.atmosphere.models.WeatherData
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {


    @GET("/data/2.5/forecast?id=524901&lang=ru&appid=520173c51a58fa1ce4c277f097f932d0")
   suspend fun getData(): WeatherData
}