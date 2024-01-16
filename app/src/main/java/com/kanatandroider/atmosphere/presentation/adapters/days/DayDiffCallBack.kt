package com.kanatandroider.atmosphere.presentation.adapters.days

import androidx.recyclerview.widget.DiffUtil
import com.kanatandroider.atmosphere.domain.models.ForcastDayEntity
import com.kanatandroider.atmosphere.domain.models.HourEntity


object DayDiffCallBack : DiffUtil.ItemCallback<ForcastDayEntity>() {

    override fun areItemsTheSame(oldItem: ForcastDayEntity, newItem: ForcastDayEntity): Boolean {
        return oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: ForcastDayEntity, newItem: ForcastDayEntity): Boolean {
        return oldItem == newItem
    }
}