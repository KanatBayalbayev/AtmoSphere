package com.kanatandroider.atmosphere.models


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class WeatherData(
    @SerializedName("cod")
    @Expose
    val cod: String,
    @SerializedName("message")
    @Expose
    val message: Int,
    @SerializedName("cnt")
    @Expose
    val cnt: Int,
    @SerializedName("list")
    @Expose
    val list: List<EachData>,
    @SerializedName("city")
    @Expose
    val city: City
)