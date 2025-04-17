package dev.android.atmosphere.domain.usecase

import android.util.Log
import dev.android.atmosphere.domain.model.DataState
import dev.android.atmosphere.domain.model.Forecast
import dev.android.atmosphere.domain.repository.LocationRepository
import dev.android.atmosphere.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetForecastUseCase(
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository
) {

    operator fun invoke(days: Int = 5): Flow<DataState<Forecast>> = flow {
        Log.d("WeatherRepositoryImpl", "GetForecastUseCase invoke")
        emit(DataState.Loading)

        val locationFlow = locationRepository.getCurrentLocation()
        var locationSuccess = false

        locationFlow.collect { locationResource ->
            when (locationResource) {
                is DataState.Success -> {
                    locationSuccess = true
                    val location = locationResource.data
                    Log.d("WeatherRepositoryImpl", "location to getForecast latitude: ${location.latitude} longitude: ${location.longitude}")
                    weatherRepository.getForecast(location.latitude, location.longitude, days)
                        .collect { forecastResource ->
                            Log.d("WeatherRepositoryImpl", "forecastResource: $forecastResource")
                            emit(forecastResource)
                        }
                }

                is DataState.Error -> {
                    Log.d("WeatherRepositoryImpl", "locationFlow Error : ${locationResource.message}")
                    if (!locationSuccess) {
                        Log.d("WeatherRepositoryImpl", "!locationSuccess: ${!locationSuccess}")
                        weatherRepository.getLastSavedForecast().collect { forecastResource ->
                            emit(forecastResource)
                        }
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
        return weatherRepository.getForecast(latitude, longitude, days)
    }

    fun byCity(cityName: String, days: Int = 5): Flow<DataState<Forecast>> {
        return weatherRepository.getForecastByCity(cityName, days)
    }

    fun getLastSaved(): Flow<DataState<Forecast>> {
        return weatherRepository.getLastSavedForecast()
    }
}