package com.kanatandroider.atmosphere.domain.usecases

import com.kanatandroider.atmosphere.domain.repository.WeatherRepository
import javax.inject.Inject

class GetCurrentWeatherListUseCase @Inject constructor(
    private val repository: WeatherRepository
) {

    operator fun invoke() = repository.getCurrentWeatherList()

}