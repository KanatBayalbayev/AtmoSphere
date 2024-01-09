package com.kanatandroider.atmosphere.domain.models


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Hour(
    @SerializedName("time_epoch")
    @Expose
    val timeEpoch: Int,
    @SerializedName("time")
    @Expose
    val time: String,
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
    @SerializedName("snow_cm")
    @Expose
    val snowCm: Double,
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
    @SerializedName("windchill_c")
    @Expose
    val windchillC: Double,
    @SerializedName("windchill_f")
    @Expose
    val windchillF: Double,
    @SerializedName("heatindex_c")
    @Expose
    val heatindexC: Double,
    @SerializedName("heatindex_f")
    @Expose
    val heatindexF: Double,
    @SerializedName("dewpoint_c")
    @Expose
    val dewpointC: Double,
    @SerializedName("dewpoint_f")
    @Expose
    val dewpointF: Double,
    @SerializedName("will_it_rain")
    @Expose
    val willItRain: Int,
    @SerializedName("chance_of_rain")
    @Expose
    val chanceOfRain: Int,
    @SerializedName("will_it_snow")
    @Expose
    val willItSnow: Int,
    @SerializedName("chance_of_snow")
    @Expose
    val chanceOfSnow: Int,
    @SerializedName("vis_km")
    @Expose
    val visKm: Double,
    @SerializedName("vis_miles")
    @Expose
    val visMiles: Double,
    @SerializedName("gust_mph")
    @Expose
    val gustMph: Double,
    @SerializedName("gust_kph")
    @Expose
    val gustKph: Double,
    @SerializedName("uv")
    @Expose
    val uv: Double
)