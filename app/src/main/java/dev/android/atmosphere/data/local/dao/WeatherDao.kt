package dev.android.atmosphere.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import dev.android.atmosphere.data.local.entity.DailyForecastEntity
import dev.android.atmosphere.data.local.entity.HourlyForecastEntity
import dev.android.atmosphere.data.local.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity): Long

    @Query("SELECT * FROM weather ORDER BY timestamp DESC LIMIT 1")
    fun getLastWeather(): Flow<WeatherEntity?>

    @Query("SELECT * FROM weather WHERE cityName = :cityName ORDER BY timestamp DESC LIMIT 1")
    fun getWeatherByCity(cityName: String): Flow<WeatherEntity?>

    @Query("DELETE FROM weather WHERE cityName = :cityName")
    suspend fun deleteWeatherByCity(cityName: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyForecast(dailyForecast: DailyForecastEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyForecasts(dailyForecasts: List<DailyForecastEntity>)

    @Query("SELECT * FROM daily_forecast WHERE cityName = :cityName ORDER BY date ASC")
    fun getDailyForecastByCity(cityName: String): Flow<List<DailyForecastEntity>>

    @Query("DELETE FROM daily_forecast WHERE cityName = :cityName")
    suspend fun deleteDailyForecastByCity(cityName: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourlyForecast(hourlyForecast: HourlyForecastEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourlyForecasts(hourlyForecasts: List<HourlyForecastEntity>)

    @Query("SELECT * FROM hourly_forecast WHERE cityName = :cityName ORDER BY time ASC")
    fun getHourlyForecastByCity(cityName: String): Flow<List<HourlyForecastEntity>>

    @Query("SELECT * FROM hourly_forecast WHERE cityName = :cityName AND forecastDate = :date ORDER BY time ASC")
    fun getHourlyForecastByCityAndDate(cityName: String, date: LocalDate): Flow<List<HourlyForecastEntity>>

    @Query("DELETE FROM hourly_forecast WHERE cityName = :cityName")
    suspend fun deleteHourlyForecastByCity(cityName: String)

    @Transaction
    suspend fun clearForecastsForCity(cityName: String) {
        deleteDailyForecastByCity(cityName)
        deleteHourlyForecastByCity(cityName)
    }
}