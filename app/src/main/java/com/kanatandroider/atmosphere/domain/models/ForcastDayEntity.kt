package com.kanatandroider.atmosphere.domain.models

import com.kanatandroider.atmosphere.data.api.models.AstroDTO
import com.kanatandroider.atmosphere.data.api.models.DayDTO
import com.kanatandroider.atmosphere.data.api.models.HourDTO


data class ForcastDayEntity(
    val date: String,
    val day: DayEntity,
    val astro: AstroEntity,
    val hour: List<HourEntity>
)