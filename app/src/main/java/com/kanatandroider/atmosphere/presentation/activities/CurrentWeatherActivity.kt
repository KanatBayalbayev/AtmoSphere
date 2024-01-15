package com.kanatandroider.atmosphere.presentation.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.kanatandroider.atmosphere.R
import com.kanatandroider.atmosphere.databinding.ActivityCurrentWeatherBinding
import com.kanatandroider.atmosphere.databinding.ActivityMainBinding
import com.kanatandroider.atmosphere.presentation.utils.SharedPreferencesManager
import com.kanatandroider.atmosphere.presentation.viewmodel.MainViewModel
import com.kanatandroider.atmosphere.presentation.viewmodel.MainViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CurrentWeatherActivity : AppCompatActivity() {


    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory

    private lateinit var mainViewModel: MainViewModel
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var binding: ActivityCurrentWeatherBinding

    private val component by lazy {
        (application as MyApplication).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityCurrentWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferencesManager = SharedPreferencesManager(this)
        mainViewModel = ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]

        val location = sharedPreferencesManager.getLocation("location", "")
        if (location != null) {
            CoroutineScope(Dispatchers.IO).launch {
//                withContext(Dispatchers.Main) {
////                    binding.progressBar.visibility = View.VISIBLE
//                }
                try {
                    mainViewModel.loadData(location)
                    withContext(Dispatchers.Main) {
                        mainViewModel.currentWeatherData.observe(this@CurrentWeatherActivity) {
                            Log.d("CurrentWeatherActivity", it.toString())
                            binding.cityName.visibility = View.VISIBLE
                            binding.cityName.text = it.name
                            for (day in it.days) {
                                for (hour in day.hour) {
                                    Log.d("CurrentWeatherActivity", hour.toString())
                                }
                            }
//                            binding.progressBar.visibility = View.GONE
                        }
                    }
                } catch (e: Exception){
                    withContext(Dispatchers.Main) {
                        mainViewModel.currentWeatherData.observe(this@CurrentWeatherActivity) {
                            if (it == null){
                                Log.d("CurrentWeatherActivity", "it is null")
                            }
                            Log.d("CurrentWeatherActivity", it.toString())
                            binding.cityName.visibility = View.VISIBLE
                            binding.cityName.text = it.name
                            for (day in it.days) {
                                for (hour in day.hour) {
                                    Log.d("CurrentWeatherActivity", hour.toString())
                                }
                            }
                        }
                        Toast.makeText(this@CurrentWeatherActivity, "Ошибка: Нет интернет соединения", Toast.LENGTH_LONG).show()
                    }

                }

            }
        }
    }



    @Deprecated("Deprecated in Java",
        ReplaceWith("super.onBackPressed()", "androidx.appcompat.app.AppCompatActivity")
    )
    override fun onBackPressed() {
        Log.d("CurrentWeatherActivity", "onBackPressed")
        finishAffinity()
        super.onBackPressed()

    }
}