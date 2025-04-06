package dev.android.atmosphere.data.remote.dto

data class GeoDbResponse(
    val data: List<GeoDbCity> = emptyList(),
    val metadata: GeoDbMetadata = GeoDbMetadata()
)

data class GeoDbCity(
    val id: Long = 0,
    val wikiDataId: String = "",
    val type: String = "",
    val city: String = "",
    val name: String = "",
    val country: String = "",
    val countryCode: String = "",
    val region: String = "",
    val regionCode: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val population: Int = 0
)

data class GeoDbMetadata(
    val currentOffset: Int = 0,
    val totalCount: Int = 0
)