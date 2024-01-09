package com.kanatandroider.atmosphere.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface WeatherDAO {

    @Query("SELECT * FROM current_weather")
    fun getHourlyWeatherList(): LiveData<List<CurrentWeatherDatabase>>
}