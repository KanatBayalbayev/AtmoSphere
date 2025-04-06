package dev.android.atmosphere.domain.repository

import dev.android.atmosphere.domain.model.City

interface CityRepository {

    suspend fun searchCities(query: String): List<City>
}