package com.kanatandroider.atmosphere.domain.models


import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import com.kanatandroider.atmosphere.data.api.models.ConditionDTO
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
){
    fun getHourFromDateTimeString(): Int {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val date = format.parse(time)
        val calendar = Calendar.getInstance().apply {
            if (date != null) {
                time = date
            }
        }
        return calendar.get(Calendar.HOUR_OF_DAY)
    }
}