package dev.android.atmosphere.data.repository

import android.content.Context
import android.util.Log
import dev.android.atmosphere.data.remote.api.GeoDbApiService
import dev.android.atmosphere.domain.model.City
import dev.android.atmosphere.domain.repository.CityRepository

class CityRepositoryImpl(
    private val geoDbApiService: GeoDbApiService
) : CityRepository {

    override suspend fun searchCities(query: String): List<City> {
        if (query.length < 2) return emptyList()

        return try {
            val response = geoDbApiService.searchCities(
                apiKey = "84bd127e27msh97824982de541aep1dc7fajsn80e644ca0957",
                namePrefix = query
            )

            response.data.map { geoDbCity ->
                City(
                    id = geoDbCity.id,
                    name = geoDbCity.city,
                    country = geoDbCity.country,
                    latitude = geoDbCity.latitude,
                    longitude = geoDbCity.longitude
                )
            }
        } catch (e: Exception) {
            Log.e("CityRepository", "Error searching cities: ${e.message}", e)
            emptyList()
        }
    }

    // Остальные методы...
}