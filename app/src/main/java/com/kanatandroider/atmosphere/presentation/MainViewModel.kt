package com.kanatandroider.atmosphere.presentation

import androidx.lifecycle.ViewModel
import com.kanatandroider.atmosphere.domain.usecases.GetCurrentWeatherListUseCase
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getCurrentWeatherListUseCase: GetCurrentWeatherListUseCase
): ViewModel() {

    val currentWeatherData = getCurrentWeatherListUseCase()


}