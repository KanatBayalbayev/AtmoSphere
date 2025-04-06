package dev.android.atmosphere.data.remote.api

import dev.android.atmosphere.data.remote.dto.GeoDbResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GeoDbApiService {

    @GET("v1/geo/cities")
    suspend fun searchCities(
        @Header("X-RapidAPI-Key") apiKey: String,
        @Header("X-RapidAPI-Host") host: String = "wft-geo-db.p.rapidapi.com",
        @Query("namePrefix") namePrefix: String,
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0,
        @Query("sort") sort: String = "-population"
    ): GeoDbResponse
}