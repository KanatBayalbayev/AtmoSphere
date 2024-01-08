package com.kanatandroider.atmosphere.models


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Wind(
    @SerializedName("speed")
    @Expose
    val speed: Double,
    @SerializedName("deg")
    @Expose
    val deg: Int,
    @SerializedName("gust")
    @Expose
    val gust: Double
)