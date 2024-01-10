package com.kanatandroider.atmosphere.data.mapper

import com.kanatandroider.atmosphere.data.api.models.ForecastdayDTO
import com.kanatandroider.atmosphere.data.api.models.HourDTO
import com.kanatandroider.atmosphere.data.api.models.WeatherDataDTO
import com.kanatandroider.atmosphere.data.database.CurrentWeatherDatabase
import com.kanatandroider.atmosphere.domain.CurrentWeatherEntity
import javax.inject.Inject

class WeatherMapper @Inject constructor() {


//    fun mapWeatherDTOToDatabase(
//        weatherDataDTO: WeatherDataDTO,
//    ): CurrentWeatherDatabase{
//        return CurrentWeatherDatabase(
//            name = weatherDataDTO.location.name,
//            lastUpdated = weatherDataDTO.current.lastUpdated,
//            currentTempC = weatherDataDTO.current.tempC,
//            maxTempC = forecastdayDTO.day.maxtempC,
//            minTempC = forecastdayDTO.day.mintempC,
//            windKph = weatherDataDTO.current.windKph,
//            humidity = weatherDataDTO.current.humidity,
//            feelsLikeC = weatherDataDTO.current.feelslikeC,
//            description = weatherDataDTO.current.condition.text,
//            codeOfDescription = weatherDataDTO.current.condition.code,
//            dailyChanceOfRain = forecastdayDTO.day.dailyChanceOfRain,
//            sunrise = forecastdayDTO.astro.sunrise,
//            sunset = forecastdayDTO.astro.sunset,
//            moonrise = forecastdayDTO.astro.moonrise,
//            moonset = forecastdayDTO.astro.moonset,
//            hourlyTime = hourDTO.time,
//            hourlyTempC = hourDTO.tempC
//        )
//    }


    fun mapWeatherDTOToDatabase(
        weatherDataDTO: WeatherDataDTO,
    ): CurrentWeatherDatabase{
        return CurrentWeatherDatabase(
            name = weatherDataDTO.location.name,
            lastUpdated = weatherDataDTO.current.lastUpdated,
            currentTempC = weatherDataDTO.current.tempC,
            windKph = weatherDataDTO.current.windKph,
            humidity = weatherDataDTO.current.humidity,
            feelsLikeC = weatherDataDTO.current.feelslikeC,
            description = weatherDataDTO.current.condition.text,
            codeOfDescription = weatherDataDTO.current.condition.code,
        )
    }


//    fun mapDatabaseToEntity(
//        currentWeatherDatabase: CurrentWeatherDatabase
//    ): CurrentWeatherEntity{
//        return CurrentWeatherEntity(
//            name = currentWeatherDatabase.name,
//            lastUpdated = currentWeatherDatabase.lastUpdated,
//            currentTempC = currentWeatherDatabase.currentTempC,
//            maxTempC = currentWeatherDatabase.maxTempC,
//            minTempC = currentWeatherDatabase.minTempC,
//            windKph = currentWeatherDatabase.windKph,
//            humidity = currentWeatherDatabase.humidity,
//            feelsLikeC = currentWeatherDatabase.feelsLikeC,
//            description = currentWeatherDatabase.description,
//            codeOfDescription = currentWeatherDatabase.codeOfDescription,
//            dailyChanceOfRain = currentWeatherDatabase.dailyChanceOfRain,
//            sunrise = currentWeatherDatabase.sunrise,
//            sunset = currentWeatherDatabase.sunset,
//            moonrise = currentWeatherDatabase.moonrise,
//            moonset = currentWeatherDatabase.moonset,
//            hourlyTime = currentWeatherDatabase.hourlyTime,
//            hourlyTempC = currentWeatherDatabase.hourlyTempC
//        )
//    }

    fun mapDatabaseToEntity(
        currentWeatherDatabase: CurrentWeatherDatabase
    ): CurrentWeatherEntity{
        return CurrentWeatherEntity(
            name = currentWeatherDatabase.name,
            lastUpdated = currentWeatherDatabase.lastUpdated,
            currentTempC = currentWeatherDatabase.currentTempC,
            windKph = currentWeatherDatabase.windKph,
            humidity = currentWeatherDatabase.humidity,
            feelsLikeC = currentWeatherDatabase.feelsLikeC,
            description = currentWeatherDatabase.description,
            codeOfDescription = currentWeatherDatabase.codeOfDescription
        )
    }

}