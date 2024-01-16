package com.kanatandroider.atmosphere.data.repositoryimpl

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kanatandroider.atmosphere.data.api.network.ApiService
import com.kanatandroider.atmosphere.data.database.WeatherDAO
import com.kanatandroider.atmosphere.data.mapper.WeatherMapper
import com.kanatandroider.atmosphere.domain.CurrentWeatherEntity
import com.kanatandroider.atmosphere.domain.models.ForcastDayEntity
import com.kanatandroider.atmosphere.domain.models.HourEntity
import com.kanatandroider.atmosphere.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val mapper: WeatherMapper,
    private val weatherDAO: WeatherDAO,
    private val apiService: ApiService,
    private val gson: Gson
) : WeatherRepository {

    override fun getCurrentWeather(): LiveData<CurrentWeatherEntity> {
        return weatherDAO.getCurrentWeather().map {
            val listType = object : TypeToken<List<ForcastDayEntity>>() {}.type
            val myObjectList: List<ForcastDayEntity> = gson.fromJson(it.forecastday, listType)
            for (data in myObjectList){
                val hours = data.hour
                Log.d("EntityfromJsontestMaker", hours.toString())
            }

            mapper.mapDatabaseToEntity(it, myObjectList)
        }
    }


    override suspend fun loadData(city: String) {
        val listData = apiService.getData(
            cityInput = city
        )
        Log.d("DTOtestMaker", listData.toString())
        val gson = Gson()
        val json = gson.toJson(listData.forecast.forecastday)
        Log.d("JSONTESTMAKER", json.toString())

        val dbList = mapper.mapWeatherDTOToDatabase(listData, json)




        weatherDAO.insertCurrentWeatherList(dbList)

        val jsonDB: String = dbList.forecastday
        val listType = object : TypeToken<List<ForcastDayEntity>>() {}.type
        val myObjectList: List<ForcastDayEntity> = gson.fromJson(json, listType)
        Log.d("CheckForWorkingTestMaker", myObjectList.toString())
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