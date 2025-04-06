package dev.android.atmosphere.presentation.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash_screen")
    data object LocationPermission : Screen("location_permission_screen")
    data object CitySelection : Screen("city_selection_screen")
    data object Weather : Screen("weather_screen")


    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}