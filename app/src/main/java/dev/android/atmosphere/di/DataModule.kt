package dev.android.atmosphere.di

import androidx.room.Room
import dev.android.atmosphere.data.local.database.WeatherDatabase
import dev.android.atmosphere.data.mapper.WeatherMapper
import dev.android.atmosphere.data.remote.api.WeatherApi
import dev.android.atmosphere.data.repository.CityRepositoryImpl
import dev.android.atmosphere.data.repository.LocationRepositoryImpl
import dev.android.atmosphere.data.repository.WeatherRepositoryImpl
import dev.android.atmosphere.domain.repository.CityRepository
import dev.android.atmosphere.domain.repository.LocationRepository
import dev.android.atmosphere.domain.repository.WeatherRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataModule = module {

    single { provideOkHttpClient() }

    single { provideRetrofit(get(), WeatherApi.BASE_URL) }

    single { provideWeatherApi(get()) }

    single(qualifier = named("geoDbRetrofit")) {
        provideGeoDbRetrofit(get())
    }
    single {
        provideGeoDbApiService(get(qualifier = named("geoDbRetrofit")))
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            WeatherDatabase::class.java,
            "weather_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<WeatherDatabase>().weatherDao() }

    single { WeatherMapper() }

    single<WeatherRepository> {
        WeatherRepositoryImpl(
            weatherApi = get(),
            weatherDao = get(),
            mapper = get(),
            networkRepository = get()
        )
    }

    single<LocationRepository> {
        LocationRepositoryImpl(
            fusedLocationProviderClient = get(),
            context = androidContext()
        )
    }

    single<CityRepository> {
        CityRepositoryImpl(
            geoDbApiService = get()
        )
    }
}