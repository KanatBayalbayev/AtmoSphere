package com.kanatandroider.atmosphere.data.workmanager

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.kanatandroider.atmosphere.data.api.network.ApiService
import com.kanatandroider.atmosphere.data.database.WeatherDAO
import com.kanatandroider.atmosphere.data.mapper.WeatherMapper
import javax.inject.Inject

class UpdateDataFactory @Inject constructor(
    private val mapper: WeatherMapper,
    private val cryptoDAO: WeatherDAO,
    private val apiService: ApiService,
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return UpdateWeatherData(
            appContext,
            workerParameters,
            mapper,
            cryptoDAO,
            apiService
        )
    }
}

