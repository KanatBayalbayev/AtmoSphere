package dev.android.atmosphere.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.android.atmosphere.presentation.screens.permission.LocationPermissionScreen
import dev.android.atmosphere.presentation.screens.splash.SplashScreen

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

        composable(route = Screen.Weather.route) {
//            WeatherScreen()
        }
    }
}