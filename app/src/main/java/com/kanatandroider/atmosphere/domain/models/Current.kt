package com.kanatandroider.atmosphere.domain.models


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Current(
    @SerializedName("last_updated_epoch")
    @Expose
    val lastUpdatedEpoch: Int,
    @SerializedName("last_updated")
    @Expose
    val lastUpdated: String,
    @SerializedName("temp_c")
    @Expose
    val tempC: Double,
    @SerializedName("temp_f")
    @Expose
    val tempF: Double,
    @SerializedName("is_day")
    @Expose
    val isDay: Int,
    @SerializedName("condition")
    @Expose
    val condition: Condition,
    @SerializedName("wind_mph")
    @Expose
    val windMph: Double,
    @SerializedName("wind_kph")
    @Expose
    val windKph: Double,
    @SerializedName("wind_degree")
    @Expose
    val windDegree: Int,
    @SerializedName("wind_dir")
    @Expose
    val windDir: String,
    @SerializedName("pressure_mb")
    @Expose
    val pressureMb: Double,
    @SerializedName("pressure_in")
    @Expose
    val pressureIn: Double,
    @SerializedName("precip_mm")
    @Expose
    val precipMm: Double,
    @SerializedName("precip_in")
    @Expose
    val precipIn: Double,
    @SerializedName("humidity")
    @Expose
    val humidity: Int,
    @SerializedName("cloud")
    @Expose
    val cloud: Int,
    @SerializedName("feelslike_c")
    @Expose
    val feelslikeC: Double,
    @SerializedName("feelslike_f")
    @Expose
    val feelslikeF: Double,
    @SerializedName("vis_km")
    @Expose
    val visKm: Double,
    @SerializedName("vis_miles")
    @Expose
    val visMiles: Double,
    @SerializedName("uv")
    @Expose
    val uv: Double,
    @SerializedName("gust_mph")
    @Expose
    val gustMph: Double,
    @SerializedName("gust_kph")
    @Expose
    val gustKph: Double
)