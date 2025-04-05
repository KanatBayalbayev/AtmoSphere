package dev.android.atmosphere.domain.usecase

import dev.android.atmosphere.domain.model.DataState
import dev.android.atmosphere.domain.model.Weather
import dev.android.atmosphere.domain.repository.LocationRepository
import dev.android.atmosphere.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RefreshWeatherUseCase(
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

                    weatherRepository.refreshWeather(location.latitude, location.longitude)
                        .collect { weatherResource ->
                            emit(weatherResource)
                        }
                }

                is DataState.Error -> {
                    if (!locationSuccess) {
                        emit(
                            DataState.Error(
                                locationResource.message ?: "Не удалось получить местоположение"
                            )
                        )
                    }
                }

                is DataState.Loading -> Unit
            }
        }
    }

    fun byCoordinates(latitude: Double, longitude: Double): Flow<DataState<Weather>> {
        return weatherRepository.refreshWeather(latitude, longitude)
    }

    fun byCity(cityName: String): Flow<DataState<Weather>> {
        return weatherRepository.getWeatherByCity(cityName)
    }
}