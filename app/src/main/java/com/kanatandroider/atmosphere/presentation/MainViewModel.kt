package com.kanatandroider.atmosphere.presentation

import androidx.lifecycle.ViewModel
import com.kanatandroider.atmosphere.data.api.network.ApiService
import com.kanatandroider.atmosphere.domain.usecases.GetCurrentWeatherListUseCase
import com.kanatandroider.atmosphere.domain.usecases.GetDataUseCase
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getCurrentWeatherListUseCase: GetCurrentWeatherListUseCase,
    private val loadDataUseCase: GetDataUseCase,
    private val apiService: ApiService
): ViewModel() {

    val currentWeatherData = getCurrentWeatherListUseCase()


    init {
        loadDataUseCase()
    }


}