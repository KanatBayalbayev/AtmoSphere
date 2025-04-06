package dev.android.atmosphere.di

import dev.android.atmosphere.presentation.screens.permission.LocationPermissionViewModel
import dev.android.atmosphere.presentation.screens.splash.SplashViewModel
import dev.android.atmosphere.presentation.screens.weather.WeatherViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel { SplashViewModel() }

    viewModel { LocationPermissionViewModel(get()) }

    viewModel {
        WeatherViewModel(
            getCurrentWeatherUseCase = get(),
            getForecastUseCase = get(),
            getLocationUseCase = get(),
            refreshWeatherUseCase = get(),
            refreshForecastUseCase = get(),
            searchCityUseCase = get()
        )
    }
}