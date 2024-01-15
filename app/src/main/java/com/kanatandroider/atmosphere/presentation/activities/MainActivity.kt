package com.kanatandroider.atmosphere.presentation.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.kanatandroider.atmosphere.R
import com.kanatandroider.atmosphere.databinding.ActivityMainBinding
import com.kanatandroider.atmosphere.presentation.viewmodel.MainViewModel
import com.kanatandroider.atmosphere.presentation.viewmodel.MainViewModelFactory
import com.kanatandroider.atmosphere.presentation.utils.SharedPreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

//    @Inject
//    lateinit var mainViewModelFactory: MainViewModelFactory

    private lateinit var binding: ActivityMainBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
//    private lateinit var mainViewModel: MainViewModel

    private val component by lazy {
        (application as MyApplication).component
    }

    private lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        sharedPreferencesManager = SharedPreferencesManager(this)




        button = findViewById(R.id.buttton)

        val currentLocale = resources.configuration.locales.get(0)
        val language = currentLocale.language
        val country = currentLocale.country
        Log.d("Lanueage", language)


        button.setOnClickListener {
            checkAndGetLocation()
        }


//        mainViewModel = ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]

//        CoroutineScope(Dispatchers.IO).launch {
//            mainViewModel.loadData("Almaty")
//            withContext(Dispatchers.Main) {
//                mainViewModel.currentWeatherData.observe(this@MainActivity) {
//                    Log.d("TestCleanArchAndDagger", it.toString())
//                    for (day in it.days) {
//                        for (hour in day.hour) {
//                            Log.d("TestCleanArchAndDaggerHours", hour.toString())
//                        }
//                    }
//                }
//
//            }
//        }

    }


    private fun checkAndGetLocation() {
        if (checkLocationPermission()) {
            getCurrentLocation()
        } else {
            requestLocationPermission()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    Log.d("MainActivityGetLocation", "onRequestPermissionsResult Доступ дали")


                    getCurrentLocation()
                    onBoardingFinished()
                    val intent = Intent(this, CurrentWeatherActivity::class.java)
                    startActivity(intent)

                } else {
                    Log.d("MainActivityGetLocation", "onRequestPermissionsResult Отказали")
                }
                return
            }
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            REQUEST_LOCATION_PERMISSION
        )

    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->

                if (location != null) {
                    Log.d("MainActivityGetLocation", "getCurrentLocation \"${location.latitude},${location.longitude}\"")
                    sharedPreferencesManager.saveLocation(
                        "location",
                        "${location.latitude},${location.longitude}"
                    )
                }

            }
    }

    private fun checkLocationPermission(): Boolean {
        val fineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        val coarseLocationPermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return fineLocationPermission == PackageManager.PERMISSION_GRANTED
                &&
                coarseLocationPermission == PackageManager.PERMISSION_GRANTED

    }

    private fun onBoardingFinished() {
        sharedPreferencesManager.saveFinishedViewPagerContainerState("FinishedViewPager", true)
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }


}

