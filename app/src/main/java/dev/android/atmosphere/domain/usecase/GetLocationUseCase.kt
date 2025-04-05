package dev.android.atmosphere.domain.usecase

import dev.android.atmosphere.domain.model.DataState
import dev.android.atmosphere.domain.model.UserLocation
import dev.android.atmosphere.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow

class GetLocationUseCase(
    private val locationRepository: LocationRepository
) {

    operator fun invoke(): Flow<DataState<UserLocation>> {
        return locationRepository.getCurrentLocation()
    }

    fun isLocationServiceEnabled(): Boolean {
        return locationRepository.isLocationServiceEnabled()
    }

    fun getLastSaved(): Flow<DataState<UserLocation>> {
        return locationRepository.getLastSavedLocation()
    }
}