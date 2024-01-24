package com.kanatandroider.atmosphere.presentation.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.kanatandroider.atmosphere.R
import com.kanatandroider.atmosphere.databinding.ActivityLocationBinding
import com.kanatandroider.atmosphere.presentation.utils.SharedPreferencesManager
import com.kanatandroider.atmosphere.presentation.viewmodel.MainViewModel
import com.kanatandroider.atmosphere.presentation.viewmodel.MainViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationActivity : AppCompatActivity() {

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory

    private lateinit var binding: ActivityLocationBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var mainViewModel: MainViewModel
    private lateinit var language: String

    private val component by lazy {
        (application as MyApplication).component
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        sharedPreferencesManager = SharedPreferencesManager(this)


        val currentLocale = resources.configuration.locales.get(0)
        language = currentLocale.language

        displayBackLogic()

        binding.getGeolocationButton.setOnClickListener {
            checkAndGetLocation()
        }



        mainViewModel = ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]

        getUserInput()
    }

    private fun displayBackLogic(){
        if (sharedPreferencesManager.getFinishedLocationState(
                "backToCurrentWeatherActivity",
                false
            )){
            binding.backToButton.visibility = View.VISIBLE
            binding.backToButton.setOnClickListener {
                val intent = Intent(this, CurrentWeatherActivity::class.java)
                startActivity(intent)
            }
        } else {
            binding.backToButton.visibility = View.GONE
        }
    }

    private fun getUserInput() {
        binding.userInputButton.setOnClickListener {
            if (binding.editTextUserInput.text.toString().trim().isEmpty()) {
                Snackbar.make(it, R.string.emptyEditText, Snackbar.LENGTH_LONG).show()
            } else {
                val userInputCity = binding.editTextUserInput.text.toString()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        mainViewModel.loadData(userInputCity, language)
                        sharedPreferencesManager.saveLocation(
                            "location",
                            userInputCity
                        )
                        onBoardingFinished()
                        withContext(Dispatchers.Main) {
                            val intent =
                                Intent(this@LocationActivity, CurrentWeatherActivity::class.java)
                            startActivity(intent)
                        }
                    } catch (_: Exception) {
                        withContext(Dispatchers.Main) {
                            Snackbar.make(
                                it,
                                R.string.failedToGetWeather,
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }

                }

            }
        }
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


                } else {
                    Snackbar.make(
                        binding.locationPage,
                        R.string.failedToGetGeoLocation,
                        Snackbar.LENGTH_LONG
                    ).show()
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
        if (isNetworkAvailable(this)){
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
                        Log.d(
                            "MainActivityGetLocation",
                            "getCurrentLocation \"${location.latitude},${location.longitude}\""
                        )
                        val intent = Intent(this, CurrentWeatherActivity::class.java)
                        startActivity(intent)
                        sharedPreferencesManager.saveLocation(
                            "location",
                            "${location.latitude},${location.longitude}"
                        )
                        onBoardingFinished()
                    }

                }
        } else {
            Snackbar.make(
                binding.locationPage,
                R.string.failedToGetWeather,
                Snackbar.LENGTH_LONG
            ).show()
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
        sharedPreferencesManager.saveFinishedLocationState("backToCurrentWeatherActivity", true)
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

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }


}

