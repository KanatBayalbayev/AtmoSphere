package com.kanatandroider.atmosphere.presentation.adapters.hours

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.kanatandroider.atmosphere.R
import com.kanatandroider.atmosphere.databinding.ViewholderHourBinding
import com.kanatandroider.atmosphere.domain.models.HourEntity

class HoursAdapter(
    private val context: Context
) : ListAdapter<HourEntity, HourViewHolder>(HourDiffCallBack) {

    var onHourClickListener: OnHourClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourViewHolder {
        val binding = ViewholderHourBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HourViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourViewHolder, position: Int) {
        val weatherHour = getItem(position)
        with(holder.binding){
            with(weatherHour){
                currentDayHourTimeTV.text = time
                currentDayHourTempTV.text = tempC.toString()
                val iconWeather = ""
//                val res = when(weatherHour.condition.code){
//                    1000 -> {context.resources.openRawResource(R.raw.sun)}
//                    1003 -> {context.resources.openRawResource(R.raw.sunandclouds)}
//                    1006,1009,1030, 1135, 1147  -> {context.resources.openRawResource(R.raw.clouds)}
//                    1063, 1072, 1087, 1168, 1171, 1192, 1195, 1198, 1201, 1243, 1246, 1273, 1276 -> {context.resources.openRawResource(R.raw.rain)}
//                    1066, 1069, 1114, 1117, 1207, 1222, 1225, 1237, 1252, 1258, 1261, 1264, 1279, 1282 -> {context.resources.openRawResource(R.raw.snow)}
//                    1150, 1153,1180, 1183, 1186, 1189, 1240 -> {context.resources.openRawResource(R.raw.rainandsun)}
//                    1204, 1210, 1213, 1216, 1219, 1249, 1255 -> {context.resources.openRawResource(R.raw.sunandsnow)}
//                    else -> {""}
//                }
                val code = weatherHour.condition.code
                if (code == 1000) {
                    currentDayHourIconIV.setAnimation(R.raw.sun)

                } else if (code == 1003) {
                    currentDayHourIconIV.setAnimation(R.raw.sunandclouds)

                } else if (code in listOf(1006, 1009, 1030, 1135, 1147)) {
                    currentDayHourIconIV.setAnimation(R.raw.clouds)

                } else if (code in listOf(1063, 1072, 1087,1168, 1171, 1192, 1195, 1198, 1201, 1243, 1246, 1273, 1276)) {
                    currentDayHourIconIV.setAnimation(R.raw.rain)

                } else if (code in listOf(1066, 1069, 1114, 1117, 1207, 1222, 1225, 1237, 1252, 1258, 1261, 1264, 1279, 1282)) {
                    currentDayHourIconIV.setAnimation(R.raw.snow)

                } else if (code in listOf(1150, 1153, 1180, 1183, 1186, 1189, 1240)) {
                    currentDayHourIconIV.setAnimation(R.raw.rainandsun)

                } else if (code in listOf(1204, 1210, 1213, 1216, 1219, 1249, 1255)) {
                    currentDayHourIconIV.setAnimation(R.raw.sunandsnow)

                } else {
                    ""
                }


                root.setOnClickListener {
                    onHourClickListener?.onHourClick(this)
                }
            }
        }

    }

    interface OnHourClickListener {
        fun onHourClick(hourEntity: HourEntity)
    }
}