package com.kanatandroider.atmosphere.domain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



data class ConditionEntity(
    val text: String,
    val code: Int
)
