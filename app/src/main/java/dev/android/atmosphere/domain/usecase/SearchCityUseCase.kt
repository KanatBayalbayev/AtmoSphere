package dev.android.atmosphere.domain.usecase

import dev.android.atmosphere.domain.model.DataState
import dev.android.atmosphere.domain.model.Forecast
import dev.android.atmosphere.domain.model.Weather
import dev.android.atmosphere.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

class SearchCityUseCase(
    private val weatherRepository: WeatherRepository
) {

    fun getWeather(cityName: String): Flow<DataState<Weather>> {
        return weatherRepository.getWeatherByCity(cityName)
    }

    fun getForecast(cityName: String, days: Int = 5): Flow<DataState<Forecast>> {
        return weatherRepository.getForecastByCity(cityName, days)
    }
}