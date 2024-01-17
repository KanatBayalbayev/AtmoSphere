package com.kanatandroider.atmosphere.presentation.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kanatandroider.atmosphere.R
import com.kanatandroider.atmosphere.databinding.ActivityCurrentWeatherBinding
import com.kanatandroider.atmosphere.domain.models.HourEntity
import com.kanatandroider.atmosphere.presentation.adapters.hours.HoursAdapter
import com.kanatandroider.atmosphere.presentation.utils.SharedPreferencesManager
import com.kanatandroider.atmosphere.presentation.viewmodel.MainViewModel
import com.kanatandroider.atmosphere.presentation.viewmodel.MainViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class CurrentWeatherActivity : AppCompatActivity() {


    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory

    private lateinit var mainViewModel: MainViewModel
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var binding: ActivityCurrentWeatherBinding
    private var currentDate: String = ""

    private val component by lazy {
        (application as MyApplication).component
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityCurrentWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferencesManager = SharedPreferencesManager(this)
        mainViewModel = ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]

        val adapter = HoursAdapter(this)
        binding.recyclerViewCurrentDayHours.adapter = adapter

        adapter.onHourClickListener = object : HoursAdapter.OnHourClickListener{
            override fun onHourClick(hourEntity: HourEntity) {
                Log.d("HourDetails", hourEntity.toString())
            }

        }
        currentDate = getCurrentDate()
        val location = sharedPreferencesManager.getLocation("location", "")
        if (location != null) {
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
//                    binding.progressBar.visibility = View.VISIBLE
                }
                try {

                    mainViewModel.loadData(location)
                    withContext(Dispatchers.Main) {
                        mainViewModel.currentWeatherData.observe(this@CurrentWeatherActivity) {
                            Log.d("CurrentWeatherActivity", it.toString())
                            binding.cityNameTV.text = it.name
                            binding.currentDayTemperatureTV.text = it.currentTempC.toString()
                            binding.currentDayWeatherFeelsLikeTV.text = it.feelsLikeC.toString()
                            binding.currentDayWeatherConditionTV.text = it.description

                            val code = it.codeOfDescription
                            if (code == 1000) {
                                binding.currentDayWeatherIconIV.setAnimation(R.raw.sun)

                            } else if (code == 1003) {
                                binding.currentDayWeatherIconIV.setAnimation(R.raw.sunandclouds)

                            } else if (code in listOf(1006, 1009, 1030, 1135, 1147)) {
                                binding.currentDayWeatherIconIV.setAnimation(R.raw.clouds)

                            } else if (code in listOf(1063, 1072, 1087,1168, 1171, 1192, 1195, 1198, 1201, 1243, 1246, 1273, 1276)) {
                                binding.currentDayWeatherIconIV.setAnimation(R.raw.rain)

                            } else if (code in listOf(1066, 1069, 1114, 1117, 1207, 1222, 1225, 1237, 1252, 1258, 1261, 1264, 1279, 1282)) {
                                binding.currentDayWeatherIconIV.setAnimation(R.raw.snow)

                            } else if (code in listOf(1150, 1153, 1180, 1183, 1186, 1189, 1240)) {
                                binding.currentDayWeatherIconIV.setAnimation(R.raw.rainandsun)

                            } else if (code in listOf(1204, 1210, 1213, 1216, 1219, 1249, 1255)) {
                                binding.currentDayWeatherIconIV.setAnimation(R.raw.sunandsnow)

                            } else {
                                ""
                            }

                            val days = it.days
                            for (day in it.days) {
                                if (day.date == currentDate){
                                    val hoursList = day.hour
                                    Log.d("hoursList", hoursList.toString())
                                    adapter.submitList(hoursList)
                                }

//                                for (hour in day.hour) {
//                                    Log.d("CurrentWeatherActivity", hour.toString())
//                                }
                            }
//                            binding.progressBar.visibility = View.GONE
                        }
                    }
                } catch (e: Exception){
                    withContext(Dispatchers.Main) {
                        mainViewModel.currentWeatherData.observe(this@CurrentWeatherActivity) {
//                            if (it == null){
//                                Log.d("CurrentWeatherActivity", "it is null")
//                            }
                            Log.d("CurrentWeatherActivity", it.toString())
//                            binding.cityName.visibility = View.VISIBLE
//                            binding.cityName.text = it.name
                            for (day in it.days) {

                                val hoursList = day.hour
                                Log.d("hoursList", hoursList.toString())
                                adapter.submitList(hoursList)
//                                for (hour in day.hour) {
//                                    Log.d("CurrentWeatherActivity", hour.toString())
//                                }
                            }
                        }
                        Toast.makeText(
                            this@CurrentWeatherActivity,
                            "Ошибка: Нет интернет соединения",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }

            }
        }

        val layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.recyclerViewCurrentDayHours.layoutManager = layoutManager

        binding.next7daysButton.setOnClickListener {
            val intent = Intent(this, NextDaysActivity::class.java)
            startActivity(intent)
        }


//        Log.d("CurrentWeatherActivityDate", currentDate)

    }



    @Deprecated("Deprecated in Java",
        ReplaceWith("super.onBackPressed()", "androidx.appcompat.app.AppCompatActivity")
    )
    override fun onBackPressed() {
        Log.d("CurrentWeatherActivity", "onBackPressed")
        finishAffinity()
        super.onBackPressed()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return currentDate.format(formatter)
    }
}