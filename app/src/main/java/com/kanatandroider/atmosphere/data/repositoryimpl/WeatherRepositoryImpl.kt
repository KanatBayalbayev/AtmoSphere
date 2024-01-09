package com.kanatandroider.atmosphere.data.repositoryimpl

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.kanatandroider.atmosphere.data.database.AppDatabase
import com.kanatandroider.atmosphere.data.mapper.WeatherMapper
import com.kanatandroider.atmosphere.domain.CurrentWeatherEntity
import com.kanatandroider.atmosphere.domain.repository.WeatherRepository

class WeatherRepositoryImpl(
    application: Application
) : WeatherRepository {

    private val weatherDAO = AppDatabase.getInstance(application).hourlyWeatherListDAO()
    private val mapper = WeatherMapper()


    override fun getCurrentWeatherList(): LiveData<List<CurrentWeatherEntity>> {
        return weatherDAO.getHourlyWeatherList().map { listOfCurrentWeatherDatabase ->
            listOfCurrentWeatherDatabase.map {
                mapper.mapDatabaseToEntity(it)
            }
        }
    }
}