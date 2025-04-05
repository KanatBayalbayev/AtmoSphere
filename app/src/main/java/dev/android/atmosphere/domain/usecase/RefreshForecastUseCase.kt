package dev.android.atmosphere.domain.usecase

import dev.android.atmosphere.domain.model.DataState
import dev.android.atmosphere.domain.model.Forecast
import dev.android.atmosphere.domain.repository.LocationRepository
import dev.android.atmosphere.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RefreshForecastUseCase(
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository
) {

    operator fun invoke(days: Int = 5): Flow<DataState<Forecast>> = flow {
        emit(DataState.Loading)

        val locationFlow = locationRepository.getCurrentLocation()
        var locationSuccess = false

        locationFlow.collect { locationResource ->
            when (locationResource) {
                is DataState.Success -> {
                    locationSuccess = true
                    val location = locationResource.data!!

                    weatherRepository.refreshForecast(location.latitude, location.longitude, days)
                        .collect { forecastResource ->
                            emit(forecastResource)
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

    fun byCoordinates(
        latitude: Double,
        longitude: Double,
        days: Int = 5
    ): Flow<DataState<Forecast>> {
        return weatherRepository.refreshForecast(latitude, longitude, days)
    }

    fun byCity(cityName: String, days: Int = 5): Flow<DataState<Forecast>> {
        return weatherRepository.getForecastByCity(
            cityName,
            days
        )
    }
}