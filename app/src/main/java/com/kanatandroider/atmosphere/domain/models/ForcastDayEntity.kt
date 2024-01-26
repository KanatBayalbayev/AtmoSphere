package com.kanatandroider.atmosphere.domain.models

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.kanatandroider.atmosphere.data.api.models.AstroDTO
import java.text.DateFormatSymbols
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


data class ForcastDayEntity(

    @SerializedName("date")
    @Expose
    val date: String,
    @SerializedName("day")
    @Expose
    val day: DayEntity,
    @SerializedName("astro")
    @Expose
    val astro: AstroDTO,
    @SerializedName("hour")
    @Expose
    val hour: List<HourEntity>

) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getDayNameOfTheWeek(): String {
//        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//        val dateReady = LocalDate.parse(date, formatter)
//        return dateReady.dayOfWeek.toString()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dateReady = LocalDate.parse(date, formatter)
        val dayOfWeek = dateReady.dayOfWeek.value
        val locale = Locale.getDefault()
        val weekdays = DateFormatSymbols(locale).weekdays
        return weekdays[dayOfWeek + 0]
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatMonthAndDay(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(date, formatter)
        return date.format(DateTimeFormatter.ofPattern("MMM, dd"))
    }

}