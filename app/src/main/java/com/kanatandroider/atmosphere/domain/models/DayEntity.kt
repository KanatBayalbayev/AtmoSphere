package com.kanatandroider.atmosphere.domain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.kanatandroider.atmosphere.data.api.models.ConditionDTO


data class DayEntity(
    @SerializedName("maxtemp_c")
    @Expose
    val maxtempC: Double,
    @SerializedName("mintemp_c")
    @Expose
    val mintempC: Double,
    @SerializedName("daily_chance_of_rain")
    @Expose
    val dailyChanceOfRain: Int,
    @SerializedName("condition")
    @Expose
    val condition: ConditionDTO,
)
