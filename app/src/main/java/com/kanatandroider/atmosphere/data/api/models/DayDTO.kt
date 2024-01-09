package com.kanatandroider.atmosphere.data.api.models


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class DayDTO(
    @SerializedName("maxtemp_c")
    @Expose
    val maxtempC: Double,
    @SerializedName("maxtemp_f")
    @Expose
    val maxtempF: Double,
    @SerializedName("mintemp_c")
    @Expose
    val mintempC: Double,
    @SerializedName("mintemp_f")
    @Expose
    val mintempF: Double,
    @SerializedName("avgtemp_c")
    @Expose
    val avgtempC: Double,
    @SerializedName("avgtemp_f")
    @Expose
    val avgtempF: Double,
    @SerializedName("maxwind_mph")
    @Expose
    val maxwindMph: Double,
    @SerializedName("maxwind_kph")
    @Expose
    val maxwindKph: Double,
    @SerializedName("totalprecip_mm")
    @Expose
    val totalprecipMm: Double,
    @SerializedName("totalprecip_in")
    @Expose
    val totalprecipIn: Double,
    @SerializedName("totalsnow_cm")
    @Expose
    val totalsnowCm: Double,
    @SerializedName("avgvis_km")
    @Expose
    val avgvisKm: Double,
    @SerializedName("avgvis_miles")
    @Expose
    val avgvisMiles: Double,
    @SerializedName("avghumidity")
    @Expose
    val avghumidity: Int,
    @SerializedName("daily_will_it_rain")
    @Expose
    val dailyWillItRain: Int,
    @SerializedName("daily_chance_of_rain")
    @Expose
    val dailyChanceOfRain: Int,
    @SerializedName("daily_will_it_snow")
    @Expose
    val dailyWillItSnow: Int,
    @SerializedName("daily_chance_of_snow")
    @Expose
    val dailyChanceOfSnow: Int,
    @SerializedName("condition")
    @Expose
    val condition: ConditionDTO,
    @SerializedName("uv")
    @Expose
    val uv: Double
)