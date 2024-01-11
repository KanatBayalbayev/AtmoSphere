package com.kanatandroider.atmosphere.data.api.models


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class CurrentDTO(
    @SerializedName("last_updated")
    @Expose
    val lastUpdated: String,
    @SerializedName("temp_c")
    @Expose
    val tempC: Double,
    @SerializedName("condition")
    @Expose
    val condition: ConditionDTO,
    @SerializedName("wind_kph")
    @Expose
    val windKph: Double,
    @SerializedName("humidity")
    @Expose
    val humidity: Int,
    @SerializedName("feelslike_c")
    @Expose
    val feelslikeC: Double,
)