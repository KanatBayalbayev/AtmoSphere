package com.kanatandroider.atmosphere.domain.repository

import androidx.lifecycle.LiveData
import com.kanatandroider.atmosphere.domain.CurrentWeatherEntity

interface WeatherRepository {

    fun getCurrentWeatherList(): LiveData<List<CurrentWeatherEntity>>



}