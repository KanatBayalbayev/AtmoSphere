package com.kanatandroider.atmosphere.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "current_weather")
data class CurrentWeatherDatabase(
    @PrimaryKey
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("last_updated")
    @Expose
    val lastUpdated: String,
    @SerializedName("temp_c")
    @Expose
    val currentTempC: Double,
    @SerializedName("wind_kph")
    @Expose
    val windKph: Double,
    @SerializedName("humidity")
    @Expose
    val humidity: Int,
    @SerializedName("feelslike_c")
    @Expose
    val feelsLikeC: Double,
    @SerializedName("text")
    @Expose
    val description: String,
    @SerializedName("code")
    @Expose
    val codeOfDescription: Int,
    )
