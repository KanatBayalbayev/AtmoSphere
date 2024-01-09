package com.kanatandroider.atmosphere.domain.models


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class WeatherData(
    @SerializedName("location")
    @Expose
    val location: Location,
    @SerializedName("current")
    @Expose
    val current: Current,
    @SerializedName("forecast")
    @Expose
    val forecast: Forecast
)