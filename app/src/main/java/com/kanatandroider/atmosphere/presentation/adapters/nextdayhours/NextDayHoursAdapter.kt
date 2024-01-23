package com.kanatandroider.atmosphere.presentation.adapters.nextdayhours

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ListAdapter
import com.kanatandroider.atmosphere.R
import com.kanatandroider.atmosphere.databinding.ViewholderHourBinding
import com.kanatandroider.atmosphere.databinding.ViewholderNextDayHoursBinding
import com.kanatandroider.atmosphere.domain.models.ForcastDayEntity
import com.kanatandroider.atmosphere.domain.models.HourEntity
import kotlin.math.roundToInt

class NextDayHoursAdapter(
    private val context: Context
) : ListAdapter<HourEntity, NextDayHourViewHolder>(NextDayHourDiffCallBack) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NextDayHourViewHolder {
        val binding = ViewholderNextDayHoursBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NextDayHourViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: NextDayHourViewHolder, position: Int) {
        val weatherNextDayHour = getItem(position)
        with(holder.binding){
            with(weatherNextDayHour){
                val chosenDayHourTemp = context.getString(R.string.chosenDayHourTemp)

                chosenDayHourTV.text = getHours(time)
                chosenDayDateTV.text = weatherNextDayHour.formatMonthAndDay()
                chosenDayHourTempTV.text = String.format(chosenDayHourTemp, tempC.roundToInt())

                val code = weatherNextDayHour.condition.code
                if (code == 1000) {
                    chosenDayIconIV.setAnimation(R.raw.sun)

                } else if (code == 1003) {
                    chosenDayIconIV.setAnimation(R.raw.sunandclouds)

                } else if (code in listOf(1006, 1009, 1030, 1135, 1147)) {
                    chosenDayIconIV.setAnimation(R.raw.clouds)

                } else if (code in listOf(1063, 1072, 1087,1168, 1171, 1192, 1195, 1198, 1201, 1243, 1246, 1273, 1276)) {
                    chosenDayIconIV.setAnimation(R.raw.rain)

                } else if (code in listOf(1066, 1069, 1114, 1117, 1207, 1222, 1225, 1237, 1252, 1258, 1261, 1264, 1279, 1282)) {
                    chosenDayIconIV.setAnimation(R.raw.snow)

                } else if (code in listOf(1150, 1153, 1180, 1183, 1186, 1189, 1240)) {
                    chosenDayIconIV.setAnimation(R.raw.rainandsun)

                } else if (code in listOf(1204, 1210, 1213, 1216, 1219, 1249, 1255)) {
                    chosenDayIconIV.setAnimation(R.raw.sunandsnow)

                } else {
                    ""
                }

            }
        }

    }

    fun getHours(time: String): String {
        val parts = time.split(" ")
        return parts.getOrNull(1).toString()
    }

}