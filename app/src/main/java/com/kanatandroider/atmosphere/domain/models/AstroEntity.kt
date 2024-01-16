package com.kanatandroider.atmosphere.domain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class AstroEntity(
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
