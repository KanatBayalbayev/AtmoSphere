package com.kanatandroider.atmosphere.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeatherDAO {

    @Query("SELECT * FROM current_weather WHERE name == :city")
    fun getCurrentWeather(city: String): LiveData<CurrentWeatherDatabase>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeatherList(currentWeatherDatabase: CurrentWeatherDatabase)
}