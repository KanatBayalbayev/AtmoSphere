package com.kanatandroider.atmosphere.presentation.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.kanatandroider.atmosphere.R
import com.kanatandroider.atmosphere.databinding.ActivityNextDayDetailsBinding
import com.kanatandroider.atmosphere.databinding.ActivityNextDaysBinding
import com.kanatandroider.atmosphere.presentation.adapters.hours.HoursAdapter
import com.kanatandroider.atmosphere.presentation.adapters.nextdayhours.NextDayHoursAdapter
import com.kanatandroider.atmosphere.presentation.utils.SharedPreferencesManager
import com.kanatandroider.atmosphere.presentation.viewmodel.MainViewModel
import com.kanatandroider.atmosphere.presentation.viewmodel.MainViewModelFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
import kotlin.math.roundToInt

class NextDayDetailsActivity : AppCompatActivity() {

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory

    private lateinit var mainViewModel: MainViewModel

    private lateinit var binding: ActivityNextDayDetailsBinding

    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    private val component by lazy {
        (application as MyApplication).component
    }


    @RequiresApi(Build.VERSION_CODES.O)
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


        if (nextDayDate != null) {
            observeViewModel(nextDayDate, adapter)
        }

        displayNextDaysActivity()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeViewModel(nextDayDate: String, adapter: NextDayHoursAdapter){
        mainViewModel.currentWeatherData.observe(this) {
            val days = it.days

            for (day in days) {
                if (day.date == nextDayDate) {
                    val hoursList = day.hour
                    val chosenDayHour = hoursList[15]
                    adapter.submitList(hoursList)


                    val chosenDayHumidityPercent = getString(R.string.chosenDayHumiditypercent)
                    val chosenDayDailyChanceOfRain = getString(R.string.chosenDayDailyChanceOfRain)
                    val chosenDayWindKm = getString(R.string.chosenDayWindKm)
                    val chosenDayMaxTemp = getString(R.string.chosenDayMaxTemp)
                    val chosenDayMinTemp = getString(R.string.chosenDayMinTemp)

                    binding.chosenDayDate.text = formatMonthAndDay(day.date)

                    val animationResource = when (day.day.condition.code) {
                        1000 -> R.raw.sun
                        1003 -> R.raw.sunandclouds
                        in listOf(1006, 1009, 1030, 1135, 1147) -> R.raw.clouds
                        in listOf(1063, 1072, 1087, 1168, 1171, 1192, 1195, 1198, 1201, 1243, 1246, 1273, 1276) -> R.raw.rain
                        in listOf(1066, 1069, 1114, 1117, 1207, 1222, 1225, 1237, 1252, 1258, 1261, 1264, 1279, 1282) -> R.raw.snow
                        in listOf(1150, 1153, 1180, 1183, 1186, 1189, 1240) -> R.raw.rainandsun
                        in listOf(1204, 1210, 1213, 1216, 1219, 1249, 1255) -> R.raw.sunandsnow
                        else -> null
                    }

                    animationResource?.let { binding.chosenDateIconIV.setAnimation(it) }

                    binding.chosenDayPercentOfRainTV.text = String.format(
                        chosenDayDailyChanceOfRain,
                        day.day.dailyChanceOfRain
                    )

                    binding.chosenDayHumidityTV.text = String.format(
                        chosenDayHumidityPercent,
                        chosenDayHour.humidity
                    )
                    binding.chosenDayWindSpeedTV.text = String.format(
                        chosenDayWindKm,
                        chosenDayHour.windKph
                    )
                    binding.chosenDateMaxTemp.text = String.format(
                        chosenDayMaxTemp,
                        day.day.maxtempC.roundToInt()
                    )
                    binding.chosenDateMinTemp.text = String.format(
                        chosenDayMinTemp,
                        day.day.mintempC.roundToInt()
                    )
                    binding.chosenDateCondition.text = day.day.condition.text



                }
            }
        }
    }
    private fun displayNextDaysActivity(){
        binding.backToNextDaysButton.setOnClickListener {
            val intent = Intent(this, NextDaysActivity::class.java)
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatMonthAndDay(userData: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(userData, formatter)
        return date.format(DateTimeFormatter.ofPattern("MMM, dd"))
    }
}