package com.kanatandroider.atmosphere.presentation.adapters.days

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.kanatandroider.atmosphere.databinding.ViewholderDayBinding
import com.kanatandroider.atmosphere.databinding.ViewholderHourBinding
import com.kanatandroider.atmosphere.domain.models.ForcastDayEntity
import com.kanatandroider.atmosphere.domain.models.HourEntity

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

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val weatherDay = getItem(position)
        with(holder.binding){
            with(weatherDay){
                futureDayDayTV.text = date
                futureDayConditionTV.text = day.condition.text
                futureDayMaxTempTV.text = day.maxtempC.toString()
                futureDayMinTempTV.text = day.mintempC.toString()


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