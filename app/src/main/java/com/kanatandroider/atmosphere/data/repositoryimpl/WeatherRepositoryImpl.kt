package com.kanatandroider.atmosphere.data.repositoryimpl

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.kanatandroider.atmosphere.data.api.network.ApiService
import com.kanatandroider.atmosphere.data.database.AppDatabase
import com.kanatandroider.atmosphere.data.database.WeatherDAO
import com.kanatandroider.atmosphere.data.mapper.WeatherMapper
import com.kanatandroider.atmosphere.domain.CurrentWeatherEntity
import com.kanatandroider.atmosphere.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val application: Application,
    private val weatherDAO: WeatherDAO,
    private val mapper: WeatherMapper,
    private val apiService: ApiService
) : WeatherRepository {

    override fun getCurrentWeatherList(): LiveData<List<CurrentWeatherEntity>> {
        return weatherDAO.getHourlyWeatherList().map { listOfCurrentWeatherDatabase ->
            listOfCurrentWeatherDatabase.map {
                mapper.mapDatabaseToEntity(it)
            }
        }
    }

    override fun loadData() {
//        apiService.getData()

    }

}