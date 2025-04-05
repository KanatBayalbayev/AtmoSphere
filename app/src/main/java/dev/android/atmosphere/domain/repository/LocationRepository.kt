package dev.android.atmosphere.domain.repository

import dev.android.atmosphere.domain.model.DataState
import dev.android.atmosphere.domain.model.UserLocation
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    fun getCurrentLocation(): Flow<DataState<UserLocation>>

    fun isLocationServiceEnabled(): Boolean

    suspend fun saveLocation(location: UserLocation)

    fun getLastSavedLocation(): Flow<DataState<UserLocation>>
}