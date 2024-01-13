package com.kanatandroider.atmosphere.di

import android.app.Application
import com.google.android.gms.common.api.Api
import com.google.gson.Gson
import com.kanatandroider.atmosphere.data.api.network.ApiFactory
import com.kanatandroider.atmosphere.data.api.network.ApiService
import com.kanatandroider.atmosphere.data.database.AppDatabase
import com.kanatandroider.atmosphere.data.database.WeatherDAO
import com.kanatandroider.atmosphere.data.repositoryimpl.WeatherRepositoryImpl
import com.kanatandroider.atmosphere.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {


    @Binds
    @ApplicationScope
    fun bindWeatherRepository(weatherRepositoryImpl: WeatherRepositoryImpl): WeatherRepository

    companion object {

        @Provides
        @ApplicationScope
        fun provideWeatherDAO(application: Application): WeatherDAO {
            return AppDatabase.getInstance(application).weatherDAO()
        }

        @Provides
        @ApplicationScope
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }

        @Provides
        fun provideGson(): Gson {
            return Gson()
        }

    }


}