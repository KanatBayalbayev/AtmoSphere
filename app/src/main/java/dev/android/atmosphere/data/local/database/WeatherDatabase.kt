package dev.android.atmosphere.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.android.atmosphere.data.local.dao.WeatherDao
import dev.android.atmosphere.data.local.entity.DailyForecastEntity
import dev.android.atmosphere.data.local.entity.HourlyForecastEntity
import dev.android.atmosphere.data.local.entity.WeatherEntity
import dev.android.atmosphere.data.util.DateConverter

@Database(
    entities = [
        WeatherEntity::class,
        DailyForecastEntity::class,
        HourlyForecastEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}