package com.kanatandroider.atmosphere.presentation.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import com.kanatandroider.atmosphere.R
import com.kanatandroider.atmosphere.databinding.ActivityNextDaysBinding
import com.kanatandroider.atmosphere.domain.models.ForcastDayEntity
import com.kanatandroider.atmosphere.presentation.adapters.days.DaysAdapter
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
import kotlin.math.roundToInt

class NextDaysActivity : AppCompatActivity() {

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory

    private lateinit var mainViewModel: MainViewModel

    private lateinit var binding: ActivityNextDaysBinding

    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    private var currentDate: String = ""


    private val component by lazy {
        (application as MyApplication).component
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityNextDaysBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]

        sharedPreferencesManager = SharedPreferencesManager(this)

        currentDate = getCurrentDate()

        val adapter = DaysAdapter(this)


        binding.recyclerViewNextDaysRV.adapter = adapter

        adapter.onDayClickListener = object : DaysAdapter.OnDayClickListener{
            override fun onDayClick(forcastDayEntity: ForcastDayEntity) {
                sharedPreferencesManager.saveNextDayDate(
                    "nextDayDate",
                    forcastDayEntity.date
                )
                val intent = Intent(this@NextDaysActivity, NextDayDetailsActivity::class.java)
                startActivity(intent)

            }
        }

        observeViewModel(adapter)

        displayCurrentWeatherActivity()


    }

    private fun displayCurrentWeatherActivity(){
        binding.backToCurrentDayButton.setOnClickListener {
            val intent = Intent(this, CurrentWeatherActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeViewModel(adapter: DaysAdapter){
        mainViewModel.currentWeatherData.observe(this){ currentWeatherEntity ->
            val allDays = currentWeatherEntity.days
            val nextDay = currentWeatherEntity.days[1]
            val hoursNextDay = nextDay.hour[15]
            val days = allDays.filter { it.date != currentDate}
            adapter.submitList(days)

            val nextDayHumidityPercent = getString(R.string.nextDayHumiditypercent)
            val nextDayDailyChanceOfRain = getString(R.string.nextDayDailyChanceOfRain)
            val nextDayWindKm = getString(R.string.nextDayWindKm)
            val nextDayMaxTemp = getString(R.string.nextDayMaxTemp)
            val nextDayMinTemp = getString(R.string.nextDayMinTemp)


            val animationResource = when (nextDay.day.condition.code) {
                1000 -> R.raw.sun
                1003 -> R.raw.sunandclouds
                in listOf(1006, 1009, 1030, 1135, 1147) -> R.raw.clouds
                in listOf(1063, 1072, 1087, 1168, 1171, 1192, 1195, 1198, 1201, 1243, 1246, 1273, 1276) -> R.raw.rain
                in listOf(1066, 1069, 1114, 1117, 1207, 1222, 1225, 1237, 1252, 1258, 1261, 1264, 1279, 1282) -> R.raw.snow
                in listOf(1150, 1153, 1180, 1183, 1186, 1189, 1240) -> R.raw.rainandsun
                in listOf(1204, 1210, 1213, 1216, 1219, 1249, 1255) -> R.raw.sunandsnow
                else -> null
            }

            animationResource?.let { binding.tomorrowIconIV.setAnimation(it) }

            binding.nextDayPercentOfRainTV.text = String.format(
                nextDayDailyChanceOfRain,
                nextDay.day.dailyChanceOfRain
            )

            binding.nextDayHumidityTV.text = String.format(
                nextDayHumidityPercent,
                hoursNextDay.humidity
            )
            binding.nextDayWindSpeedTV.text = String.format(
                nextDayWindKm,
                hoursNextDay.windKph
            )
            binding.nextDayMaxTemp.text = String.format(
                nextDayMaxTemp,
                nextDay.day.maxtempC.roundToInt()
            )
            binding.nextDayMinTemp.text = String.format(
                nextDayMinTemp,
                nextDay.day.mintempC.roundToInt()
            )
            binding.nextDayCondition.text = nextDay.day.condition.text

        }
    }

    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("super.onBackPressed()", "androidx.appcompat.app.AppCompatActivity")
    )
    override fun onBackPressed() {
        val intent = Intent(this, CurrentWeatherActivity::class.java)
        startActivity(intent)
        super.onBackPressed()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return currentDate.format(formatter)
    }
}