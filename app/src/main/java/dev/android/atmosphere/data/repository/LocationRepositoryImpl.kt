package dev.android.atmosphere.data.repository

import android.content.Context
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Looper
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import dev.android.atmosphere.domain.model.DataState
import dev.android.atmosphere.domain.model.UserLocation
import dev.android.atmosphere.domain.repository.LocationRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "location_prefs")

class LocationRepositoryImpl(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val context: Context
) : LocationRepository {

    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    override fun getCurrentLocation(): Flow<DataState<UserLocation>> = callbackFlow {
        trySend(DataState.Loading)

        if (!isLocationServiceEnabled()) {
            trySend(DataState.Error("Службы геолокации отключены"))
            close()
            return@callbackFlow
        }

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(5000)
            .setMaxUpdateDelayMillis(10000)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                result.lastLocation?.let { location ->
                    val userLocation = UserLocation(
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                    trySend(DataState.Success(userLocation))

                    // Сохраняем местоположение в кэш
                    launch {
                        saveLocation(userLocation)
                    }
                }
            }
        }

        try {
            // Сначала пробуем получить последнее известное местоположение
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        val userLocation = UserLocation(
                            latitude = location.latitude,
                            longitude = location.longitude
                        )
                        trySend(DataState.Success(userLocation))

                        // Сохраняем местоположение в кэш
                        launch {
                            saveLocation(userLocation)
                        }
                    } else {
                        // Если последнее местоположение недоступно, запрашиваем обновления
                        fusedLocationProviderClient.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            Looper.getMainLooper()
                        )
                    }
                }
                .addOnFailureListener { e ->
                    trySend(DataState.Error("Не удалось получить местоположение: ${e.message}"))
                }
        } catch (e: Exception) {
            trySend(DataState.Error("Ошибка местоположения: ${e.message}"))
        }

        awaitClose {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    override fun isLocationServiceEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override suspend fun saveLocation(location: UserLocation) {
        context.dataStore.edit { preferences ->
            preferences[LATITUDE_KEY] = location.latitude
            preferences[LONGITUDE_KEY] = location.longitude
        }
    }

    override  fun getLastSavedLocation(): Flow<DataState<UserLocation>> = flow {
        emit(DataState.Loading)

        try {
            val preferences = context.dataStore.data.first()
            val latitude = preferences[LATITUDE_KEY]
            val longitude = preferences[LONGITUDE_KEY]

            if (latitude != null && longitude != null) {
                emit(DataState.Success(UserLocation(latitude, longitude)))
            } else {
                emit(DataState.Error("Нет сохраненных данных о местоположении"))
            }
        } catch (e: Exception) {
            emit(DataState.Error("Ошибка при получении сохраненного местоположения: ${e.message}"))
        }
    }

    private companion object {
        private val LATITUDE_KEY = doublePreferencesKey("last_latitude")
        private val LONGITUDE_KEY = doublePreferencesKey("last_longitude")
    }
}