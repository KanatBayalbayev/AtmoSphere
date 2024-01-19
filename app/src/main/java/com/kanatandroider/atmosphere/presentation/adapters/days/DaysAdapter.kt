package com.kanatandroider.atmosphere.presentation.adapters.days

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ListAdapter
import com.kanatandroider.atmosphere.R
import com.kanatandroider.atmosphere.databinding.ViewholderDayBinding
import com.kanatandroider.atmosphere.databinding.ViewholderHourBinding
import com.kanatandroider.atmosphere.domain.models.ForcastDayEntity
import com.kanatandroider.atmosphere.domain.models.HourEntity
import kotlin.math.roundToInt

class DaysAdapter(
    private val context: Context
) : ListAdapter<ForcastDayEntity, DayViewHolder>(DayDiffCallBack) {

    var onDayClickListener: OnDayClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val binding = ViewholderDayBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DayViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val weatherDay = getItem(position)
        with(holder.binding) {
            with(weatherDay) {
                val futureDayMaxTemp = context.getString(R.string.futureDayMaxTemp)
                val futureDayMinTemp = context.getString(R.string.futureDayMinTemp)
                futureDayDayNameTV.text = weatherDay.getDayNameOfTheWeek()
                futureDayDayDateTV.text = weatherDay.formatMonthAndDay()
                futureDayMaxTempTV.text = String.format(futureDayMaxTemp, day.maxtempC.roundToInt())
                futureDayMinTempTV.text = String.format(futureDayMinTemp, day.mintempC.roundToInt())

                val code = weatherDay.day.condition.code
                if (code == 1000) {
                    futureDayIV.setAnimation(R.raw.sun)

                } else if (code == 1003) {
                    futureDayIV.setAnimation(R.raw.sunandclouds)

                } else if (code in listOf(1006, 1009, 1030, 1135, 1147)) {
                    futureDayIV.setAnimation(R.raw.clouds)

                } else if (code in listOf(
                        1063,
                        1072,
                        1087,
                        1168,
                        1171,
                        1192,
                        1195,
                        1198,
                        1201,
                        1243,
                        1246,
                        1273,
                        1276
                    )
                ) {
                    futureDayIV.setAnimation(R.raw.rain)

                } else if (code in listOf(
                        1066,
                        1069,
                        1114,
                        1117,
                        1207,
                        1222,
                        1225,
                        1237,
                        1252,
                        1258,
                        1261,
                        1264,
                        1279,
                        1282
                    )
                ) {
                    futureDayIV.setAnimation(R.raw.snow)

                } else if (code in listOf(1150, 1153, 1180, 1183, 1186, 1189, 1240)) {
                    futureDayIV.setAnimation(R.raw.rainandsun)

                } else if (code in listOf(1204, 1210, 1213, 1216, 1219, 1249, 1255)) {
                    futureDayIV.setAnimation(R.raw.sunandsnow)

                } else {
                    ""
                }

                root.setOnClickListener {
                    onDayClickListener?.onDayClick(this)
                }
            }
        }

    }

    interface OnDayClickListener {
        fun onDayClick(forcastDayEntity: ForcastDayEntity)
    }
}