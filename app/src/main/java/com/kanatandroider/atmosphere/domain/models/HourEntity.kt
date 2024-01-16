package com.kanatandroider.atmosphere.domain.models


import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import com.kanatandroider.atmosphere.data.api.models.ConditionDTO

data class HourEntity(
    @PrimaryKey
    @SerializedName("time")
    @Expose
    val time: String,
    @SerializedName("temp_c")
    @Expose
    val tempC: Double,
    @SerializedName("condition")
    @Expose
    val condition: ConditionEntity,
    @SerializedName("wind_kph")
    @Expose
    val windKph: Double,
    @SerializedName("humidity")
    @Expose
    val humidity: Int,
    @SerializedName("feelslike_c")
    @Expose
    val feelslikeC: Double,
    @SerializedName("chance_of_rain")
    @Expose
    val chanceOfRain: Int,
)