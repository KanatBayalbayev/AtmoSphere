package dev.android.atmosphere.domain.model

data class City(
    val id: Long = 0,
    val city: String = "",
    val name: String = "",
    val country: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)