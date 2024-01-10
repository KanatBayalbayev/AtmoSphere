package com.kanatandroider.atmosphere.data.repositoryimpl

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.kanatandroider.atmosphere.data.api.network.ApiService
import com.kanatandroider.atmosphere.data.database.WeatherDAO
import com.kanatandroider.atmosphere.data.mapper.WeatherMapper
import com.kanatandroider.atmosphere.domain.CurrentWeatherEntity
import com.kanatandroider.atmosphere.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val mapper: WeatherMapper,
    private val weatherDAO: WeatherDAO,
    private val apiService: ApiService
) : WeatherRepository {

    override fun getCurrentWeatherList(city: String): LiveData<CurrentWeatherEntity> {
        return weatherDAO.getCurrentWeather(city).map {
            mapper.mapDatabaseToEntity(it)
        }
    }

    override suspend fun loadData(city: String) {
        val listData = apiService.getData(
            cityInput = city,
        )
        Log.d("DTOtestMaker", listData.toString())
        val dbList = mapper.mapWeatherDTOToDatabase(listData)
        Log.d("DBtestMaker", dbList.toString())
        weatherDAO.insertCurrentWeatherList(dbList)
        val entityData = mapper.mapDatabaseToEntity(dbList)
        Log.d("EntitytestMaker", entityData.toString())
    }
//    override fun getCurrentWeatherList(): LiveData<CurrentWeatherEntity> {
////        return weatherDAO.getHourlyWeatherList().map {
////            Log.d("WeatherRepositoryImpl", it.toString())
////        }
//        //TODO
//    }

//    override suspend fun loadData() {

//        val workManager = WorkManager.getInstance(application)
//        workManager.enqueueUniqueWork(
//            UpdateWeatherData.NAME,
//            ExistingWorkPolicy.REPLACE,
//            UpdateWeatherData.makeRequest()
//        )
//
//        val listData = apiService.getData()
//        Log.d("DTOtestMaker", listData.toString())
//        val dbList = mapper.mapWeatherDTOToDatabase(listData)
//        Log.d("DBtestMaker", dbList.toString())
//        weatherDAO.insertCurrentWeatherList(dbList)
//        val entityData = mapper.mapDatabaseToEntity(dbList)
//        Log.d("EntitytestMaker", entityData.toString())


//    }

}