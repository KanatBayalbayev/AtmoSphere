package dev.android.atmosphere.di

import dev.android.atmosphere.domain.usecase.GetCurrentWeatherUseCase
import dev.android.atmosphere.domain.usecase.GetForecastUseCase
import dev.android.atmosphere.domain.usecase.GetLocationUseCase
import dev.android.atmosphere.domain.usecase.RefreshForecastUseCase
import dev.android.atmosphere.domain.usecase.RefreshWeatherUseCase
import dev.android.atmosphere.domain.usecase.SearchCitiesUseCase
import dev.android.atmosphere.domain.usecase.SearchCityUseCase
import org.koin.dsl.module

val domainModule = module {

    factory { GetCurrentWeatherUseCase(get(), get()) }
    factory { GetForecastUseCase(get(), get()) }
    factory { GetLocationUseCase(get()) }

    factory { RefreshWeatherUseCase(get(), get()) }
    factory { RefreshForecastUseCase(get(), get()) }

    factory { SearchCityUseCase(get()) }

    factory { SearchCitiesUseCase(get()) }
}