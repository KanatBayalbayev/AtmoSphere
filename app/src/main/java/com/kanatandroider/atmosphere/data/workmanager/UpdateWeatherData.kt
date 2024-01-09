package com.kanatandroider.atmosphere.data.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import com.kanatandroider.atmosphere.data.api.network.ApiService
import com.kanatandroider.atmosphere.data.database.WeatherDAO
import com.kanatandroider.atmosphere.data.mapper.WeatherMapper
import kotlinx.coroutines.delay

class UpdateWeatherData(
    context: Context,
    workerParameters: WorkerParameters,
    private val mapper: WeatherMapper,
    private val weatherDAO: WeatherDAO,
    private val apiService: ApiService
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        while (true) {
            try {
                val apiList = apiService.getData()
                val dbModelList = mapper.mapWeatherDTOToDatabase(
                    apiList,
                    apiList.forecast.forecastday[1],
                    apiList.forecast.forecastday[1].hour[1]
                )
                weatherDAO.insertCurrentWeatherList(dbModelList)
            } catch (e: Exception) {
                TODO("Not yet implemented")
            }
            delay(10000)
        }
    }

    companion object {

        const val NAME = "RefreshDataWorker"

        fun makeRequest(): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<UpdateWeatherData>().build()
        }
    }
}