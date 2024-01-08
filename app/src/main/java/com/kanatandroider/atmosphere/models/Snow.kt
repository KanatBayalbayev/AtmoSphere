package com.kanatandroider.atmosphere.models


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Snow(
    @SerializedName("3h")
    @Expose
    val h: Double
)