package dev.android.atmosphere.domain.usecase

import dev.android.atmosphere.domain.model.DataState
import dev.android.atmosphere.domain.model.Weather
import dev.android.atmosphere.domain.repository.LocationRepository
import dev.android.atmosphere.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetCurrentWeatherUseCase(
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository
) {

    operator fun invoke(): Flow<DataState<Weather>> = flow {
        emit(DataState.Loading)

        val locationFlow = locationRepository.getCurrentLocation()
        var locationSuccess = false

        locationFlow.collect { locationResource ->
            when (locationResource) {
                is DataState.Success -> {
                    locationSuccess = true
                    val location = locationResource.data

                    weatherRepository.getCurrentWeather(location.latitude, location.longitude)
                        .collect { weatherResource ->
                            emit(weatherResource)
                        }
                }

                is DataState.Error -> {
                    if (!locationSuccess) {
                        weatherRepository.getLastSavedWeather().collect { weatherResource ->
                            emit(weatherResource)
                        }
                    }
                }

                is DataState.Loading -> Unit
            }
        }
    }

    fun byCoordinates(latitude: Double, longitude: Double): Flow<DataState<Weather>> {
        return weatherRepository.getCurrentWeather(latitude, longitude)
    }

    fun byCity(cityName: String): Flow<DataState<Weather>> {
        return weatherRepository.getWeatherByCity(cityName)
    }

    fun getLastSaved(): Flow<DataState<Weather>> {
        return weatherRepository.getLastSavedWeather()
    }
}