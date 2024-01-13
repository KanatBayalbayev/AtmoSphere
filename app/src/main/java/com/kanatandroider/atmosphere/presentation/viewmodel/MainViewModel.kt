package com.kanatandroider.atmosphere.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.kanatandroider.atmosphere.domain.usecases.GetCurrentWeatherUseCase
import com.kanatandroider.atmosphere.domain.usecases.GetDataUseCase
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val loadDataUseCase: GetDataUseCase,
): ViewModel() {


    val currentWeatherData = getCurrentWeatherUseCase()

    val loadData = loadDataUseCase

//    init {
//        viewModelScope.launch {
//            loadDataUseCase("Almaty")
//        }
//
//    }


}