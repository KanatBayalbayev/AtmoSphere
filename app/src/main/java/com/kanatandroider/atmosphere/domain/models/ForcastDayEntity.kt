package com.kanatandroider.atmosphere.domain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.kanatandroider.atmosphere.data.api.models.AstroDTO
import com.kanatandroider.atmosphere.data.api.models.DayDTO
import com.kanatandroider.atmosphere.data.api.models.HourDTO


data class ForcastDayEntity(
    @SerializedName("date")
    @Expose
    val date: String,
    @SerializedName("day")
    @Expose
    val day: DayEntity,
    @SerializedName("astro")
    @Expose
    val astro: AstroDTO,
    @SerializedName("hour")
    @Expose
    val hour: List<HourEntity>
)