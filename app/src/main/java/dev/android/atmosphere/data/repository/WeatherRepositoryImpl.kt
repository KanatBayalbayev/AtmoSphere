package dev.android.atmosphere.data.repository

import dev.android.atmosphere.data.local.dao.WeatherDao
import dev.android.atmosphere.data.mapper.WeatherMapper
import dev.android.atmosphere.data.remote.api.WeatherApi
import dev.android.atmosphere.data.remote.wrapper.ApiResult
import dev.android.atmosphere.data.util.ErrorMessages.NO_CACHED_FORECAST
import dev.android.atmosphere.data.util.ErrorMessages.NO_CACHED_WEATHER
import dev.android.atmosphere.data.util.ErrorMessages.NO_NETWORK_FOR_UPDATE
import dev.android.atmosphere.data.util.ErrorMessages.formatCacheError
import dev.android.atmosphere.data.util.ErrorMessages.formatNoCityData
import dev.android.atmosphere.domain.model.Forecast
import dev.android.atmosphere.domain.model.DataState
import dev.android.atmosphere.domain.model.Weather
import dev.android.atmosphere.domain.repository.NetworkRepository
import dev.android.atmosphere.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class WeatherRepositoryImpl(
    private val weatherApi: WeatherApi,
    private val weatherDao: WeatherDao,
    private val mapper: WeatherMapper,
    private val networkRepository: NetworkRepository
) : WeatherRepository {

    override fun getCurrentWeather(
        latitude: Double,
        longitude: Double
    ): Flow<DataState<Weather>> = flow {
        emit(DataState.Loading)

        if (!networkRepository.isNetworkAvailable()) {
            emitCachedWeather(this)
            return@flow
        }

        val response = ApiResult.safeApiCall {
            val coordinates = "$latitude,$longitude"
            weatherApi.getCurrentWeatherByCoordinates(coordinates)
        }

        when (response) {
            is ApiResult.Success -> {
                val weather = mapper.mapToDomainWeather(response.data)
                cacheWeather(weather)
                emit(DataState.Success(weather))
            }

            is ApiResult.Error -> emitCachedWeather(this)

            is ApiResult.Loading -> Unit
        }
    }

    override fun getForecast(
        latitude: Double,
        longitude: Double,
        days: Int
    ): Flow<DataState<Forecast>> = flow {
        emit(DataState.Loading)

        if (!networkRepository.isNetworkAvailable()) {
            emitCachedForecast(this)
            return@flow
        }

        val response = ApiResult.safeApiCall {
            val coordinates = "$latitude,$longitude"
            weatherApi.getForecastByCoordinates(coordinates, days)
        }

        when (response) {
            is ApiResult.Success -> {
                val forecast = mapper.mapToDomainForecast(response.data)
                saveForecastToDb(forecast)
                emit(DataState.Success(forecast))
            }

            is ApiResult.Error -> emitCachedForecast(this)

            is ApiResult.Loading -> Unit
        }
    }

    override fun getWeatherByCity(cityName: String): Flow<DataState<Weather>> = flow {
        emit(DataState.Loading)

        if (!networkRepository.isNetworkAvailable()) {
            emitCachedWeatherByCity(this, cityName)
            return@flow
        }

        val response = ApiResult.safeApiCall {
            weatherApi.getCurrentWeatherByCity(cityName)
        }

        when (response) {
            is ApiResult.Success -> {
                val weather = mapper.mapToDomainWeather(response.data)
                cacheWeather(weather)
                emit(DataState.Success(weather))
            }

            is ApiResult.Error -> emitCachedWeatherByCity(this, cityName)

            is ApiResult.Loading -> Unit
        }
    }

    override fun getForecastByCity(cityName: String, days: Int): Flow<DataState<Forecast>> =
        flow {
            emit(DataState.Loading)

            if (!networkRepository.isNetworkAvailable()) {
                emitCachedForecastByCity(this, cityName)
                return@flow
            }

            val response = ApiResult.safeApiCall {
                weatherApi.getForecastByCity(cityName, days)
            }

            when (response) {
                is ApiResult.Success -> {
                    val forecast = mapper.mapToDomainForecast(response.data)
                    saveForecastToDb(forecast)

                    emit(DataState.Success(forecast))
                }

                is ApiResult.Error -> emitCachedForecastByCity(this, cityName)

                is ApiResult.Loading -> Unit
            }
        }

    override fun getLastSavedWeather(): Flow<DataState<Weather>> = flow {
        emit(DataState.Loading)

        try {
            weatherDao.getLastWeather().collect { entity ->
                if (entity != null) {
                    emit(DataState.Success(mapper.mapToDomainWeather(entity)))
                } else {
                    emit(DataState.Error(NO_CACHED_WEATHER))
                }
            }
        } catch (e: Exception) {
            emit(DataState.Error(formatCacheError(e.message)))
        }
    }

    override fun getLastSavedForecast(): Flow<DataState<Forecast>> = flow {
        emit(DataState.Loading)

        try {
            val weatherEntity = weatherDao.getLastWeather().first()
            if (weatherEntity != null) {
                val cityName = weatherEntity.cityName
                val dailyForecasts = weatherDao.getDailyForecastByCity(cityName).first()
                val hourlyForecasts = weatherDao.getHourlyForecastByCity(cityName).first()

                if (dailyForecasts.isNotEmpty() || hourlyForecasts.isNotEmpty()) {
                    val forecast =
                        mapper.mapToDomainForecast(weatherEntity, dailyForecasts, hourlyForecasts)
                    emit(DataState.Success(forecast))
                } else {
                    emit(DataState.Error(NO_CACHED_FORECAST))
                }
            } else {
                emit(DataState.Error(NO_CACHED_WEATHER))
            }
        } catch (e: Exception) {
            emit(DataState.Error(formatCacheError(e.message)))
        }
    }

    override fun refreshWeather(
        latitude: Double,
        longitude: Double
    ): Flow<DataState<Weather>> = flow {
        emit(DataState.Loading)

        if (!networkRepository.isNetworkAvailable()) {
            emit(DataState.Error(NO_NETWORK_FOR_UPDATE))
            return@flow
        }

        val response = ApiResult.safeApiCall {
            val coordinates = "$latitude,$longitude"
            weatherApi.getCurrentWeatherByCoordinates(coordinates)
        }

        when (response) {
            is ApiResult.Success -> {
                val weather = mapper.mapToDomainWeather(response.data)
                val entity = mapper.mapToWeatherEntity(weather)
                weatherDao.insertWeather(entity)

                emit(DataState.Success(weather))
            }

            is ApiResult.Error -> emit(DataState.Error(NO_NETWORK_FOR_UPDATE))

            is ApiResult.Loading -> Unit
        }
    }

    override fun refreshForecast(
        latitude: Double,
        longitude: Double,
        days: Int
    ): Flow<DataState<Forecast>> = flow {
        emit(DataState.Loading)

        if (!networkRepository.isNetworkAvailable()) {
            emit(DataState.Error(NO_NETWORK_FOR_UPDATE))
            return@flow
        }

        val response = ApiResult.safeApiCall {
            val coordinates = "$latitude,$longitude"
            weatherApi.getForecastByCoordinates(coordinates, days)
        }

        when (response) {
            is ApiResult.Success -> {
                val forecast = mapper.mapToDomainForecast(response.data)

                saveForecastToDb(forecast)

                emit(DataState.Success(forecast))
            }

            is ApiResult.Error -> emit(DataState.Error(NO_NETWORK_FOR_UPDATE))

            is ApiResult.Loading -> Unit
        }
    }

    private suspend fun saveForecastToDb(forecast: Forecast) {
        val cityName = forecast.cityName

        forecast.currentWeather?.let { weather ->
            val weatherEntity = mapper.mapToWeatherEntity(weather)
            weatherDao.insertWeather(weatherEntity)
        }

        weatherDao.clearForecastsForCity(cityName)

        val dailyEntities = forecast.dailyForecasts.map { dailyForecast ->
            mapper.mapToDailyForecastEntity(dailyForecast, cityName)
        }
        weatherDao.insertDailyForecasts(dailyEntities)

        val hourlyEntities = forecast.hourlyForecasts.map { hourlyForecast ->
            mapper.mapToHourlyForecastEntity(hourlyForecast, cityName)
        }
        weatherDao.insertHourlyForecasts(hourlyEntities)
    }

    private suspend fun cacheWeather(weather: Weather) {
        val entity = mapper.mapToWeatherEntity(weather)
        weatherDao.insertWeather(entity)
    }

    private suspend fun emitCachedWeather(collector: FlowCollector<DataState<Weather>>) {
        getLastSavedWeather().collect { resource ->
            collector.emit(resource)
        }
    }

    private suspend fun emitCachedForecast(collector: FlowCollector<DataState<Forecast>>) {
        getLastSavedForecast().collect { resource ->
            collector.emit(resource)
        }
    }

    private suspend fun emitCachedWeatherByCity(
        collector: FlowCollector<DataState<Weather>>,
        cityName: String
    ) {
        weatherDao.getWeatherByCity(cityName).collect { entity ->
            if (entity != null) {
                collector.emit(DataState.Success(mapper.mapToDomainWeather(entity)))
            } else {
                collector.emit(DataState.Error(formatNoCityData(cityName)))
            }
        }
    }

    private suspend fun emitCachedForecastByCity(
        collector: FlowCollector<DataState<Forecast>>,
        cityName: String
    ) {
        val weatherEntity = weatherDao.getWeatherByCity(cityName).first()
        val dailyForecasts = weatherDao.getDailyForecastByCity(cityName).first()
        val hourlyForecasts = weatherDao.getHourlyForecastByCity(cityName).first()

        if (dailyForecasts.isNotEmpty() || hourlyForecasts.isNotEmpty()) {
            val forecast =
                mapper.mapToDomainForecast(weatherEntity, dailyForecasts, hourlyForecasts)
            collector.emit(DataState.Success(forecast))
        } else {
            collector.emit(DataState.Error(formatNoCityData(cityName)))
        }
    }
}