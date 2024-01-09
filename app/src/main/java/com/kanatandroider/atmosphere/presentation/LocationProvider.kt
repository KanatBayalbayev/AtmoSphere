//package com.kanatandroider.atmosphere.presentation
//
//import android.Manifest
//import android.content.Context
//import android.content.pm.PackageManager
//import android.location.Location
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import com.google.android.gms.location.LocationServices
//
//class LocationProvider(
//    private val context: Context,
//    private val activity: MainActivity
//) {
//
//    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
//
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            MainActivity.REQUEST_LOCATION_PERMISSION -> {
//                if (grantResults.isNotEmpty()
//                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
//                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                    getCurrentLocation()
//                } else {
//                    // Разрешение было отклонено. Вы можете показать объяснение, если считаете это необходимым
//                }
//                return
//            }
//        }
//    }
//
//    private fun requestLocationPermission() {
//        ActivityCompat.requestPermissions(
//            activity,
//            arrayOf(
//                android.Manifest.permission.ACCESS_FINE_LOCATION,
//                android.Manifest.permission.ACCESS_COARSE_LOCATION
//            ),
//            MainActivity.REQUEST_LOCATION_PERMISSION
//        )
//
//    }
//
//    private fun getCurrentLocation() {
//        if (ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            requestLocationPermission()
//            return
//        }
//        fusedLocationClient.lastLocation
//            .addOnSuccessListener { location : Location? ->
////                CoroutineScope(Dispatchers.IO).launch {
////                    val data = service.getData(city = "$location?.latitude,$location?.longitude")
////                    Log.d("DataForWork", data.toString())
////                }
//                sharedPreferencesManager.saveString("lat", location?.latitude.toString())
//                sharedPreferencesManager.saveString("lon", location?.longitude.toString())
//
//
//                longitude = location?.longitude.toString()
//                latitude = location?.latitude.toString()
//
//
//            }
//    }
//
//    private fun checkLocationPermission(): Boolean {
//        val fineLocationPermission = ContextCompat.checkSelfPermission(
//            context,
//            android.Manifest.permission.ACCESS_FINE_LOCATION
//        )
//        val coarseLocationPermission = ContextCompat.checkSelfPermission(
//            context,
//            android.Manifest.permission.ACCESS_COARSE_LOCATION
//        )
//        return fineLocationPermission == PackageManager.PERMISSION_GRANTED
//                &&
//                coarseLocationPermission == PackageManager.PERMISSION_GRANTED
//
//    }
//
//
//    companion object {
//        const val REQUEST_LOCATION_PERMISSION = 1
//    }
//}