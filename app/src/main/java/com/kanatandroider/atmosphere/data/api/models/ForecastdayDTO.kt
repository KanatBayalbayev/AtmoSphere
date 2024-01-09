package com.kanatandroider.atmosphere.data.api.models


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class ForecastdayDTO(
    @SerializedName("date")
    @Expose
    val date: String,
    @SerializedName("date_epoch")
    @Expose
    val dateEpoch: Int,
    @SerializedName("day")
    @Expose
    val day: DayDTO,
    @SerializedName("astro")
    @Expose
    val astro: AstroDTO,
    @SerializedName("hour")
    @Expose
    val hour: List<HourDTO>
)