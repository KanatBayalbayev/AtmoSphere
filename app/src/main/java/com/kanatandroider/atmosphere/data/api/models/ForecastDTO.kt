package com.kanatandroider.atmosphere.data.api.models


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class ForecastDTO(
    @SerializedName("forecastday")
    @Expose
    val forecastday: List<ForecastdayDTO>
)