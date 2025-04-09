package dev.android.atmosphere.presentation.screens.weather

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.android.atmosphere.domain.model.DataState
import dev.android.atmosphere.domain.model.Forecast
import dev.android.atmosphere.domain.model.UserLocation
import dev.android.atmosphere.domain.model.Weather
import dev.android.atmosphere.domain.usecase.GetCurrentWeatherUseCase
import dev.android.atmosphere.domain.usecase.GetForecastUseCase
import dev.android.atmosphere.domain.usecase.GetLocationUseCase
import dev.android.atmosphere.domain.usecase.RefreshForecastUseCase
import dev.android.atmosphere.domain.usecase.RefreshWeatherUseCase
import dev.android.atmosphere.domain.usecase.SearchCityUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getForecastUseCase: GetForecastUseCase,
    private val getLocationUseCase: GetLocationUseCase,
    private val refreshWeatherUseCase: RefreshWeatherUseCase,
    private val refreshForecastUseCase: RefreshForecastUseCase,
    private val searchCityUseCase: SearchCityUseCase
) : ViewModel() {

    // Состояния для текущей погоды
    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val weatherState: StateFlow<WeatherState> = _weatherState.asStateFlow()

    // Состояния для прогноза
    private val _forecastState = MutableStateFlow<ForecastState>(ForecastState.Loading)
    val forecastState: StateFlow<ForecastState> = _forecastState.asStateFlow()

    // Состояния для местоположения
    private val _locationState = MutableStateFlow<LocationState>(LocationState.Loading)
    val locationState: StateFlow<LocationState> = _locationState.asStateFlow()

    // Строка поиска
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()


    init {
        loadWeatherData()
    }


    private fun loadWeatherData() {
        // Загрузка местоположения
        viewModelScope.launch {
            loadLocation()
        }

        // Загрузка текущей погоды в отдельной корутине
        viewModelScope.launch {
            getCurrentWeatherUseCase().collect { result ->
                when (result) {
                    is DataState.Success -> _weatherState.value = WeatherState.Success(result.data)
                    is DataState.Error -> _weatherState.value = WeatherState.Error(result.message)
                    is DataState.Loading -> _weatherState.value = WeatherState.Loading
                }
            }
        }

        // Загрузка прогноза в отдельной корутине
        viewModelScope.launch {
            getForecastUseCase().collect { result ->
                when (result) {
                    is DataState.Success -> _forecastState.value = ForecastState.Success(result.data)
                    is DataState.Error -> _forecastState.value = ForecastState.Error(result.message)
                    is DataState.Loading -> _forecastState.value = ForecastState.Loading
                }
            }
        }
    }

    /**
     * Выполняет поиск по городу и загружает его погоду
     */
    fun searchCity(query: String) {
        _searchQuery.value = query

        if (query.isBlank()) {
            // Если поисковый запрос пуст, возвращаемся к погоде по местоположению
            loadWeatherData()
            return
        }

        viewModelScope.launch {
            _weatherState.value = WeatherState.Loading
            _forecastState.value = ForecastState.Loading

            // Получаем погоду по названию города
            searchCityUseCase.getWeather(query).collect { result ->
                when (result) {
                    is DataState.Success -> _weatherState.value =
                        WeatherState.Success(result.data!!)

                    is DataState.Error -> _weatherState.value =
                        WeatherState.Error(result.message ?: "Город не найден")

                    is DataState.Loading -> _weatherState.value = WeatherState.Loading
                }
            }

            // Получаем прогноз по названию города
            searchCityUseCase.getForecast(query).collect { result ->
                when (result) {
                    is DataState.Success -> _forecastState.value =
                        ForecastState.Success(result.data!!)

                    is DataState.Error -> _forecastState.value =
                        ForecastState.Error(result.message ?: "Прогноз не найден")

                    is DataState.Loading -> _forecastState.value = ForecastState.Loading
                }
            }
        }
    }

    /**
     * Обновляет все данные о погоде
     */
    fun refreshWeatherData() {
        viewModelScope.launch {
            _weatherState.value = WeatherState.Loading
            _forecastState.value = ForecastState.Loading

            // Если был поисковый запрос, обновляем по городу
            if (_searchQuery.value.isNotBlank()) {
                searchCity(_searchQuery.value)
                return@launch
            }

            // Иначе обновляем по местоположению
            refreshWeatherUseCase().collect { result ->
                when (result) {
                    is DataState.Success -> _weatherState.value =
                        WeatherState.Success(result.data!!)

                    is DataState.Error -> _weatherState.value =
                        WeatherState.Error(result.message ?: "Ошибка обновления погоды")

                    is DataState.Loading -> _weatherState.value = WeatherState.Loading
                }
            }

            refreshForecastUseCase().collect { result ->
                when (result) {
                    is DataState.Success -> _forecastState.value =
                        ForecastState.Success(result.data!!)

                    is DataState.Error -> _forecastState.value =
                        ForecastState.Error(result.message ?: "Ошибка обновления прогноза")

                    is DataState.Loading -> _forecastState.value = ForecastState.Loading
                }
            }
        }
    }

    /**
     * Загружает местоположение пользователя
     */
    private fun loadLocation() {
        viewModelScope.launch {
            _locationState.value = LocationState.Loading

            getLocationUseCase().collect { result ->
                when (result) {
                    is DataState.Success -> _locationState.value =
                        LocationState.Success(result.data!!)

                    is DataState.Error -> _locationState.value =
                        LocationState.Error(result.message ?: "Ошибка получения местоположения")

                    is DataState.Loading -> _locationState.value = LocationState.Loading
                }
            }
        }
    }

    /**
     * Проверяет доступность службы геолокации
     */
    fun isLocationServiceEnabled(): Boolean {
        return getLocationUseCase.isLocationServiceEnabled()
    }


    sealed class WeatherState {
        object Loading : WeatherState()
        data class Success(val weather: Weather) : WeatherState()
        data class Error(val message: String) : WeatherState()
    }

    sealed class ForecastState {
        object Loading : ForecastState()
        data class Success(val forecast: Forecast) : ForecastState()
        data class Error(val message: String) : ForecastState()
    }

    sealed class LocationState {
        object Loading : LocationState()
        data class Success(val location: UserLocation) : LocationState()
        data class Error(val message: String) : LocationState()
    }

}