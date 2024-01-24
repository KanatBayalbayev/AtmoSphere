package com.kanatandroider.atmosphere.presentation.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kanatandroider.atmosphere.R
import com.kanatandroider.atmosphere.databinding.ActivityCurrentWeatherBinding
import com.kanatandroider.atmosphere.presentation.adapters.hours.HoursAdapter
import com.kanatandroider.atmosphere.presentation.utils.SharedPreferencesManager
import com.kanatandroider.atmosphere.presentation.viewmodel.MainViewModel
import com.kanatandroider.atmosphere.presentation.viewmodel.MainViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import kotlin.math.roundToInt

class CurrentWeatherActivity : AppCompatActivity() {


    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory

    private lateinit var mainViewModel: MainViewModel
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var binding: ActivityCurrentWeatherBinding
    private lateinit var language: String
    private var currentDate: String = ""
    private var location: String? = ""


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
        location = sharedPreferencesManager.getLocation("location", "")

        val currentLocale = resources.configuration.locales.get(0)
        language = currentLocale.language

        mainViewModel = ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]


        val adapter = HoursAdapter(this)
        setupRecyclerView(adapter)


        val currentTime = Calendar.getInstance()
        val currentHour = currentTime.get(Calendar.HOUR_OF_DAY)

        currentDate = getCurrentDate()

        loadData(currentHour, adapter)

        displayNext7daysActivity()

        swipeToRefresh()

        menu()

    }

    private fun loadData(currentHour: Int, adapter: HoursAdapter){
        if (location != null) {
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    onLoadingProgress()
                }
                try {
                    mainViewModel.loadData(location!!, language)
                    withContext(Dispatchers.Main) {
                        offLoadingProgress()
                        observeViewModel(currentHour, adapter)
                    }
                } catch (e: Exception) {
                    snackBarNoConnection()
                    withContext(Dispatchers.Main) {
                        offLoadingProgress()
                        observeViewModel(currentHour, adapter)
                    }
                }
            }
        }
    }

    private fun menu() {
        binding.navv.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.findLocation -> {
                    displayLocationActivity()
                    binding.mainCurrentWeatherContainer.closeDrawer(GravityCompat.START)
                }
            }

            true
        }

        binding.menu.setOnClickListener {
            binding.mainCurrentWeatherContainer.openDrawer(GravityCompat.START)
        }
    }

    private fun displayNext7daysActivity() {
        binding.next7daysButton.setOnClickListener {
            val intent = Intent(this, NextDaysActivity::class.java)
            startActivity(intent)
        }
    }

    private fun displayLocationActivity() {
        val intent = Intent(
            this,
            LocationActivity::class.java
        )
        startActivity(intent)
    }

    private fun setupRecyclerView(adapter: HoursAdapter) {
        binding.recyclerViewCurrentDayHours.adapter = adapter
        val layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.recyclerViewCurrentDayHours.layoutManager = layoutManager

    }

    private fun getHourFromDateTimeString(dateTimeString: String): Int {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val date = format.parse(dateTimeString)
        val calendar = Calendar.getInstance().apply {
            if (date != null) {
                time = date
            }
        }
        return calendar.get(Calendar.HOUR_OF_DAY)
    }

    override fun onResume() {
        super.onResume()
        if (isNetworkAvailable(this)) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    location?.let { it1 -> mainViewModel.loadData(it1, language) }
                } catch (e: Exception) {
                    snackBarNoConnection()
                    return@launch
                }
            }
            binding.swipeRefreshLayout.isRefreshing = false

        } else {
            Log.d("TestConnectIOnMaker", "isOFF")
            snackBarNoConnection()
        }


    }

    private fun onLoadingProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun offLoadingProgress() {
        binding.progressBar.visibility = View.GONE
        binding.swipeRefreshLayout.visibility = View.VISIBLE
        binding.weatherContainer.visibility = View.VISIBLE
    }

    private fun observeViewModel(currentHour: Int, adapter: HoursAdapter) {
        mainViewModel.currentWeatherData.observe(this@CurrentWeatherActivity) {
            val days = it.days
            val windKmString = getString(R.string.windKm)
            val feelinglike = getString(R.string.feelinglike)
            val humidityPercent = getString(R.string.humiditypercent)
            val dailyChanceOfRain = getString(R.string.dailyChanceOfRain)

            binding.cityNameTV.text = it.name
            binding.currentDayTemperatureTV.text =
                String.format("%s°", it.currentTempC.roundToInt())
            binding.currentDayWeatherFeelsLikeTV.text =
                String.format(feelinglike, it.feelsLikeC.roundToInt())
            binding.currentDayWeatherConditionTV.text = it.description
            binding.currentDayWindSpeedTV.text =
                String.format(windKmString, it.windKph.toString())
            binding.currentDayHumidityTV.text =
                String.format(humidityPercent, it.humidity)

            for (day in days) {
                if (day.date == currentDate) {
                    binding.currentDayPercentOfRainTV.text =
                        String.format(dailyChanceOfRain, day.day.dailyChanceOfRain)
                }
            }


            val animationResource = when (it.codeOfDescription) {
                1000 -> R.raw.sun
                1003 -> R.raw.sunandclouds
                in listOf(1006, 1009, 1030, 1135, 1147) -> R.raw.clouds
                in listOf(
                    1063,
                    1072,
                    1087,
                    1168,
                    1171,
                    1192,
                    1195,
                    1198,
                    1201,
                    1243,
                    1246,
                    1273,
                    1276
                ) -> R.raw.rain

                in listOf(
                    1066,
                    1069,
                    1114,
                    1117,
                    1207,
                    1222,
                    1225,
                    1237,
                    1252,
                    1258,
                    1261,
                    1264,
                    1279,
                    1282
                ) -> R.raw.snow

                in listOf(1150, 1153, 1180, 1183, 1186, 1189, 1240) -> R.raw.rainandsun
                in listOf(1204, 1210, 1213, 1216, 1219, 1249, 1255) -> R.raw.sunandsnow
                else -> null
            }

            animationResource?.let { binding.currentDayWeatherIconIV.setAnimation(it) }

            for (day in it.days) {
                if (day.date == currentDate) {
                    val hoursList = day.hour
                    Log.d("hoursList", hoursList.toString())
                    val indexToScroll = hoursList.indexOfFirst { weather ->
                        val weatherHour = getHourFromDateTimeString(weather.time)
                        weatherHour == currentHour
                    }
                    adapter.submitList(hoursList) {
                        binding.recyclerViewCurrentDayHours.scrollToPosition(indexToScroll)
                    }

                }
            }

        }
    }

    private fun swipeToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    Snackbar.make(
                        binding.mainCurrentWeatherContainer,
                        R.string.successConnection,
                        Snackbar.LENGTH_SHORT
                    ).show()
                    location?.let { it1 -> mainViewModel.loadData(it1, language) }
                    withContext(Dispatchers.Main) {
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                } catch (e: Exception) {
                    binding.swipeRefreshLayout.isRefreshing = false
                    snackBarNoConnection()
                    return@launch
                }
            }
        }
    }


    private fun snackBarNoConnection() {
        val snackBar = Snackbar.make(
            binding.mainCurrentWeatherContainer,
            R.string.noConnection,
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction(R.string.tryAgainButton) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    Snackbar.make(
                        binding.mainCurrentWeatherContainer,
                        R.string.successConnection,
                        Snackbar.LENGTH_SHORT
                    ).show()
                    location?.let { it1 -> mainViewModel.loadData(it1, language) }
                } catch (e: Exception) {
                    Snackbar.make(
                        binding.mainCurrentWeatherContainer,
                        R.string.failedToConnect,
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return@launch
                }
            }
        }
        snackBar.show()
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }


    @Deprecated(
        "Deprecated in Java",
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