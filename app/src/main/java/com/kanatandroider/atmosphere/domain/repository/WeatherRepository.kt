package com.kanatandroider.atmosphere.domain.repository

import androidx.lifecycle.LiveData
import com.kanatandroider.atmosphere.domain.CurrentWeatherEntity
import com.kanatandroider.atmosphere.domain.models.ForcastDayEntity
import com.kanatandroider.atmosphere.domain.models.HourEntity

interface WeatherRepository {

    fun getCurrentWeather(): LiveData<CurrentWeatherEntity>


    suspend fun loadData(city: String, language: String)


}