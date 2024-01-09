package com.kanatandroider.atmosphere.domain.usecases

import com.kanatandroider.atmosphere.domain.repository.WeatherRepository

class GetCurrentWeatherData(
    val repository: WeatherRepository
) {
    operator fun invoke() = repository.getCurrentWeatherData()
}