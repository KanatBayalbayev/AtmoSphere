package com.kanatandroider.atmosphere.domain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.kanatandroider.atmosphere.data.api.models.ConditionDTO


data class DayEntity(
    val maxtempC: Double,
    val mintempC: Double,
    val dailyChanceOfRain: Int,
    val condition: ConditionEntity,
)
