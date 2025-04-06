package dev.android.atmosphere.domain.usecase

import dev.android.atmosphere.domain.model.City
import dev.android.atmosphere.domain.model.DataState
import dev.android.atmosphere.domain.repository.CityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchCitiesUseCase(
    private val cityRepository: CityRepository
) {

    operator fun invoke(query: String): Flow<DataState<List<City>>> = flow {
        emit(DataState.Loading)

        try {

            if (query.length < 2) {
                emit(DataState.Success(emptyList()))
                return@flow
            }

            val cities = cityRepository.searchCities(query)

            emit(DataState.Success(cities))
        } catch (e: Exception) {
            emit(DataState.Error("Не удалось выполнить поиск городов: ${e.message}"))
        }
    }
}