package dev.android.atmosphere.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.android.atmosphere.presentation.screens.citySelection.CitySelectionScreen
import dev.android.atmosphere.presentation.screens.permission.LocationPermissionScreen
import dev.android.atmosphere.presentation.screens.splash.SplashScreen
import dev.android.atmosphere.presentation.screens.weather.WeatherScreen

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.LocationPermission.route
    ) {

//        composable(route = Screen.Splash.route) {
//            SplashScreen(navController = navController)
//        }

        composable(route = Screen.LocationPermission.route) {
            LocationPermissionScreen(navController = navController)
        }

        composable(route = Screen.CitySelection.route) {
            CitySelectionScreen(navController = navController)
        }

        composable(route = Screen.Weather.route) {
            WeatherScreen()
        }
    }
}