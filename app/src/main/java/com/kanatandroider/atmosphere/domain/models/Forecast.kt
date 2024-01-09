package com.kanatandroider.atmosphere.domain.models


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Forecast(
    @SerializedName("forecastday")
    @Expose
    val forecastday: List<Forecastday>
)