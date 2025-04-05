package dev.android.atmosphere.domain.repository

import dev.android.atmosphere.domain.model.Forecast
import dev.android.atmosphere.domain.model.DataState
import dev.android.atmosphere.domain.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

     fun getCurrentWeather(latitude: Double, longitude: Double): Flow<DataState<Weather>>

     fun getForecast(latitude: Double, longitude: Double, days: Int = 5): Flow<DataState<Forecast>>

     fun getWeatherByCity(cityName: String): Flow<DataState<Weather>>

     fun getForecastByCity(cityName: String, days: Int = 5): Flow<DataState<Forecast>>

     fun getLastSavedWeather(): Flow<DataState<Weather>>

     fun getLastSavedForecast(): Flow<DataState<Forecast>>

     fun refreshWeather(latitude: Double, longitude: Double): Flow<DataState<Weather>>

     fun refreshForecast(latitude: Double, longitude: Double, days: Int = 5): Flow<DataState<Forecast>>

}