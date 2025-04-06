package dev.android.atmosphere.presentation.screens.permission

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.android.atmosphere.domain.model.DataState
import dev.android.atmosphere.domain.usecase.GetLocationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocationPermissionViewModel(
    private val getLocationUseCase: GetLocationUseCase
) : ViewModel() {

    private val _permissionGranted = MutableStateFlow(false)
    val permissionGranted = _permissionGranted.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _isLocationServiceEnabled = MutableStateFlow(true)
    val isLocationServiceEnabled = _isLocationServiceEnabled.asStateFlow()

    init {
        checkLocationService()
    }

    fun onPermissionResult(granted: Boolean) {
        _permissionGranted.value = granted
        if (granted) {
            checkLocationAvailability()
        }
    }

    fun checkLocationService() {
        _isLocationServiceEnabled.value = getLocationUseCase.isLocationServiceEnabled()
    }

    private fun checkLocationAvailability() {
        viewModelScope.launch {
            _isLoading.value = true
            getLocationUseCase().collect { result ->
                when (result) {
                    is DataState.Success -> {
                        // Местоположение доступно
                        _isLoading.value = false
                        _error.value = null
                    }
                    is DataState.Error -> {
                        _isLoading.value = false
                        _error.value = result.message ?: "Не удалось получить местоположение"
                    }
                    is DataState.Loading -> {
                        _isLoading.value = true
                    }
                }
            }
        }
    }
}