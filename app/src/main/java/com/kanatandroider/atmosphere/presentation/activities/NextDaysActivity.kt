package com.kanatandroider.atmosphere.presentation.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.kanatandroider.atmosphere.databinding.ActivityNextDaysBinding
import com.kanatandroider.atmosphere.domain.models.ForcastDayEntity
import com.kanatandroider.atmosphere.presentation.adapters.days.DaysAdapter
import com.kanatandroider.atmosphere.presentation.viewmodel.MainViewModel
import com.kanatandroider.atmosphere.presentation.viewmodel.MainViewModelFactory
import javax.inject.Inject

class NextDaysActivity : AppCompatActivity() {

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory

    private lateinit var mainViewModel: MainViewModel

    private lateinit var binding: ActivityNextDaysBinding

    private val component by lazy {
        (application as MyApplication).component
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityNextDaysBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]

        val adapter = DaysAdapter(this)

        binding.recyclerViewNextDaysRV.adapter = adapter

        adapter.onDayClickListener = object : DaysAdapter.OnDayClickListener{
            override fun onDayClick(forcastDayEntity: ForcastDayEntity) {
                Log.d("DaysDetails", forcastDayEntity.toString())
            }
        }

        mainViewModel.currentWeatherData.observe(this){
            val days = it.days
            adapter.submitList(days)
        }

    }
}