package com.kanatandroider.atmosphere.data.api.models


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class DayDTO(
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