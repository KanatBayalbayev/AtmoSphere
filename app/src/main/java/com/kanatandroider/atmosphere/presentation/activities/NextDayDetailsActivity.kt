package com.kanatandroider.atmosphere.presentation.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.kanatandroider.atmosphere.R
import com.kanatandroider.atmosphere.databinding.ActivityNextDayDetailsBinding
import com.kanatandroider.atmosphere.databinding.ActivityNextDaysBinding
import com.kanatandroider.atmosphere.presentation.adapters.hours.HoursAdapter
import com.kanatandroider.atmosphere.presentation.adapters.nextdayhours.NextDayHoursAdapter
import com.kanatandroider.atmosphere.presentation.utils.SharedPreferencesManager
import com.kanatandroider.atmosphere.presentation.viewmodel.MainViewModel
import com.kanatandroider.atmosphere.presentation.viewmodel.MainViewModelFactory
import javax.inject.Inject

class NextDayDetailsActivity : AppCompatActivity() {

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory

    private lateinit var mainViewModel: MainViewModel

    private lateinit var binding: ActivityNextDayDetailsBinding

    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    private val component by lazy {
        (application as MyApplication).component
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityNextDayDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]

        sharedPreferencesManager = SharedPreferencesManager(this)

        val nextDayDate = sharedPreferencesManager.getNextDayDate("nextDayDate", "")

        val adapter = NextDayHoursAdapter(this)
        binding.recyclerViewNextDayHoursRV.adapter = adapter


        mainViewModel.currentWeatherData.observe(this) {
            val days = it.days
            for (day in days) {
                if (day.date == nextDayDate) {
                    val hoursList = day.hour
                    adapter.submitList(hoursList)
                }
            }
        }


    }
}