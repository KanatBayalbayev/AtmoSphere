package com.kanatandroider.atmosphere.data.repositoryimpl

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.kanatandroider.atmosphere.data.api.network.ApiFactory
import com.kanatandroider.atmosphere.data.api.network.ApiService
import com.kanatandroider.atmosphere.data.database.AppDatabase
import com.kanatandroider.atmosphere.data.database.WeatherDAO
import com.kanatandroider.atmosphere.data.mapper.WeatherMapper
import com.kanatandroider.atmosphere.domain.CurrentWeatherEntity
import com.kanatandroider.atmosphere.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val application: Application,
    private val weatherDAO: WeatherDAO,
    private val mapper: WeatherMapper,
    private val apiService: ApiService
) : WeatherRepository {

    override fun getCurrentWeatherList(): LiveData<List<CurrentWeatherEntity>> {

        return weatherDAO.getHourlyWeatherList().map { listOfCurrentWeatherDatabase ->
            Log.d("WeatherRepositoryImpl",listOfCurrentWeatherDatabase.toString() )
            listOfCurrentWeatherDatabase.map {
                mapper.mapDatabaseToEntity(it)
            }
        }
    }

    override fun loadData() {



    }

}