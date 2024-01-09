package com.kanatandroider.atmosphere.domain.models


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Condition(
    @SerializedName("text")
    @Expose
    val text: String,
    @SerializedName("icon")
    @Expose
    val icon: String,
    @SerializedName("code")
    @Expose
    val code: Int
)