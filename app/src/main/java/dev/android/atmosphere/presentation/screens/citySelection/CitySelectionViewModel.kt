package dev.android.atmosphere.presentation.screens.citySelection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.android.atmosphere.domain.model.City
import dev.android.atmosphere.domain.model.DataState
import dev.android.atmosphere.domain.model.UserLocation
import dev.android.atmosphere.domain.repository.LocationRepository
import dev.android.atmosphere.domain.usecase.SearchCitiesUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CitySelectionViewModel(
    private val searchCitiesUseCase: SearchCitiesUseCase,
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<DataState<List<City>>?>(null)
    val searchResults: StateFlow<DataState<List<City>>?> = _searchResults.asStateFlow()

    private val _selectedCity = MutableStateFlow<City?>(null)
    val selectedCity: StateFlow<City?> = _selectedCity.asStateFlow()

    private var searchJob: Job? = null

//    init {
//        loadPopularCities()
//    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()

        if (query.isEmpty()) {
            // Если запрос пустой, показываем популярные города
            loadPopularCities()
            return
        }

        // Добавляем небольшую задержку перед поиском, чтобы не делать запросы на каждый символ
        searchJob = viewModelScope.launch {
            _searchResults.value = DataState.Loading
            delay(300) // Задержка в 300 мс
            searchCities(query)
        }
    }

    fun selectCity(city: City) {
        viewModelScope.launch {
            // Сохраняем выбранный город в репозиторий
//            cityRepository.saveSelectedCity(city)

            // Создаем объект местоположения из координат города и сохраняем
            val location = UserLocation(latitude = city.latitude, longitude = city.longitude)
            locationRepository.saveLocation(location)

            // Устанавливаем выбранный город в StateFlow
            _selectedCity.value = city
        }
    }

    private fun loadPopularCities() {
        viewModelScope.launch {
            _searchResults.value = DataState.Loading
            try {
//                val cities = cityRepository.getPopularCities()
//                _searchResults.value = DataState.Success(cities)
            } catch (e: Exception) {
                _searchResults.value = DataState.Error("Не удалось загрузить список популярных городов: ${e.message}")
            }
        }
    }

    private fun searchCities(query: String) {
        viewModelScope.launch {
            try {
                val cities = searchCitiesUseCase(query)

                cities.collect { result ->
                    when (result) {
                        is DataState.Success -> _searchResults.value = DataState.Success(result.data)

                        is DataState.Error -> Unit

                        is DataState.Loading -> Unit
                    }
                }
            } catch (e: Exception) {
                _searchResults.value = DataState.Error("Ошибка поиска городов: ${e.message}")
            }
        }
    }
}