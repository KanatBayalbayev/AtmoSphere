package com.kanatandroider.atmosphere.presentation.adapters.nextdayhours

import androidx.recyclerview.widget.DiffUtil
import com.kanatandroider.atmosphere.domain.models.ForcastDayEntity
import com.kanatandroider.atmosphere.domain.models.HourEntity


object NextDayHourDiffCallBack : DiffUtil.ItemCallback<HourEntity>() {

    override fun areItemsTheSame(oldItem: HourEntity, newItem: HourEntity): Boolean {
        return oldItem.time == newItem.time
    }

    override fun areContentsTheSame(oldItem: HourEntity, newItem: HourEntity): Boolean {
        return oldItem == newItem
    }
}