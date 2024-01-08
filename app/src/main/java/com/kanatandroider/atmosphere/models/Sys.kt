package com.kanatandroider.atmosphere.models


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Sys(
    @SerializedName("pod")
    @Expose
    val pod: String
)