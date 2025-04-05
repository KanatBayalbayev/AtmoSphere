package dev.android.atmosphere.di

import dev.android.atmosphere.data.repository.NetworkRepositoryImpl
import dev.android.atmosphere.domain.repository.NetworkRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    single { provideContext(androidApplication())  }

    single { provideLocationClient(androidContext()) }

    single<NetworkRepository> { NetworkRepositoryImpl(androidContext()) }
}