package com.kanatandroider.atmosphere.data.api.models


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class WeatherDataDTO(
    @SerializedName("location")
    @Expose
    val location: LocationDTO,
    @SerializedName("current")
    @Expose
    val current: CurrentDTO,
    @SerializedName("forecast")
    @Expose
    val forecast: ForecastDTO
)