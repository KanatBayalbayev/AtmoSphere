package com.kanatandroider.atmosphere.data.api.models


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class AstroDTO(
    @SerializedName("sunrise")
    @Expose
    val sunrise: String,
    @SerializedName("sunset")
    @Expose
    val sunset: String,
    @SerializedName("moonrise")
    @Expose
    val moonrise: String,
    @SerializedName("moonset")
    @Expose
    val moonset: String,
    @SerializedName("moon_phase")
    @Expose
    val moonPhase: String,
    @SerializedName("moon_illumination")
    @Expose
    val moonIllumination: Int,
    @SerializedName("is_moon_up")
    @Expose
    val isMoonUp: Int,
    @SerializedName("is_sun_up")
    @Expose
    val isSunUp: Int
)