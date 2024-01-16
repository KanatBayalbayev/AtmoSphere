package com.kanatandroider.atmosphere.domain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



data class ConditionEntity(
    @SerializedName("text")
    @Expose
    val text: String,
    @SerializedName("code")
    @Expose
    val code: Int
)
