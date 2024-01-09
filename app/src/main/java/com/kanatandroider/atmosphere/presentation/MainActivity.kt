package com.kanatandroider.atmosphere.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.kanatandroider.atmosphere.R
import com.kanatandroider.atmosphere.data.api.network.ApiFactory
import com.kanatandroider.atmosphere.data.api.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private var longitude: String = ""
    private var latitude: String = ""
    private lateinit var service: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        sharedPreferencesManager = SharedPreferencesManager(this)

        val lon = sharedPreferencesManager.getString("lon", "")
        val lat = sharedPreferencesManager.getString("lat", "")

        service = ApiFactory.apiService

        CoroutineScope(Dispatchers.IO).launch {
            val data = service.getData(city = "$lat,$lon")
            Log.d("MainActivityTestMaker", data.toString())

        }







//        CoroutineScope(Dispatchers.IO).launch {

//            val data = lon?.let {
//                if (lat != null) {
//                    service.getData(lat, it)
//                }
//            }
//            Log.d("DataForWork", data.toString())
//        }

        Log.d("DataForWork", "$lat")
        Log.d("DataForWork", "$lon")

        if (checkLocationPermission()) {
            getCurrentLocation()
        } else {
            requestLocationPermission()
        }

        Log.d("LocationTestMaker", longitude)


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
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation()
                } else {
                    // Разрешение было отклонено. Вы можете показать объяснение, если считаете это необходимым
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
            requestLocationPermission()
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
//                CoroutineScope(Dispatchers.IO).launch {
//                    val data = service.getData(city = "$location?.latitude,$location?.longitude")
//                    Log.d("DataForWork", data.toString())
//                }
                sharedPreferencesManager.saveString("lat", location?.latitude.toString())
                sharedPreferencesManager.saveString("lon", location?.longitude.toString())


                longitude = location?.longitude.toString()
                latitude = location?.latitude.toString()


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

    companion object {
        const val REQUEST_LOCATION_PERMISSION = 1
    }
}