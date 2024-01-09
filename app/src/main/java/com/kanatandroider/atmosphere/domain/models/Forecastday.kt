package com.kanatandroider.atmosphere.domain.models


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Forecastday(
    @SerializedName("date")
    @Expose
    val date: String,
    @SerializedName("date_epoch")
    @Expose
    val dateEpoch: Int,
    @SerializedName("day")
    @Expose
    val day: Day,
    @SerializedName("astro")
    @Expose
    val astro: Astro,
    @SerializedName("hour")
    @Expose
    val hour: List<Hour>
)