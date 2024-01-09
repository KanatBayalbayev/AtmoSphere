package com.kanatandroider.atmosphere.domain.usecases

import com.kanatandroider.atmosphere.domain.repository.WeatherRepository
import javax.inject.Inject

class GetDataUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {

    operator fun invoke() = weatherRepository.loadData()
}