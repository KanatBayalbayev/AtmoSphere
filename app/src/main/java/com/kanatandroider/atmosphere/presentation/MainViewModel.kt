package com.kanatandroider.atmosphere.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kanatandroider.atmosphere.data.api.network.ApiService
import com.kanatandroider.atmosphere.domain.usecases.GetCurrentWeatherListUseCase
import com.kanatandroider.atmosphere.domain.usecases.GetDataUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getCurrentWeatherListUseCase: GetCurrentWeatherListUseCase,
    private val loadDataUseCase: GetDataUseCase,
): ViewModel() {


    val currentWeatherData = getCurrentWeatherListUseCase

    val loadData = loadDataUseCase

//    init {
//        viewModelScope.launch {
//            loadDataUseCase("Almaty")
//        }
//
//    }


}