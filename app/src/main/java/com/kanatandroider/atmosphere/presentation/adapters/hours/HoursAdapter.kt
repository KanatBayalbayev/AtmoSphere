package com.kanatandroider.atmosphere.presentation.adapters.hours

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
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