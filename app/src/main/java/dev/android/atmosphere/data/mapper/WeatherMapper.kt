package dev.android.atmosphere.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import dev.android.atmosphere.data.local.entity.DailyForecastEntity
import dev.android.atmosphere.data.local.entity.HourlyForecastEntity
import dev.android.atmosphere.data.local.entity.WeatherEntity
import dev.android.atmosphere.data.remote.dto.CurrentWeatherDto
import dev.android.atmosphere.data.remote.dto.ForecastDayDto
import dev.android.atmosphere.data.remote.dto.ForecastDto
import dev.android.atmosphere.data.remote.dto.HourDto
import dev.android.atmosphere.domain.model.DailyForecast
import dev.android.atmosphere.domain.model.Forecast
import dev.android.atmosphere.domain.model.HourlyForecast
import dev.android.atmosphere.domain.model.Weather
import dev.android.atmosphere.domain.model.WeatherCondition
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class WeatherMapper {


    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun mapToDomainWeather(dto: CurrentWeatherDto): Weather {
        val current = dto.current
        val location = dto.location

        val timestampDateTime = parseLocalDateTime(location.localtime)
        val lastUpdatedDateTime = parseLocalDateTime(current.lastUpdated)

        return Weather(
            cityName = location.name,
            region = location.region,
            country = location.country,
            temperature = current.tempC,
            feelsLike = current.feelslikeC,
            humidity = current.humidity,
            pressure = current.pressureMb.toInt(),
            windSpeed = current.windKph,
            windDirection = current.windDegree,
            windDirectionText = current.windDir,
            description = current.condition.text,
            icon = current.condition.icon,
            uv = current.uv,
            cloudCover = current.cloud,
            precipitation = current.precipMm,
            visibility = current.visKm,
            isDay = current.isDay == 1,
            timestamp = timestampDateTime,
            lastUpdated = lastUpdatedDateTime
        )
    }

    fun mapToDomainForecast(dto: ForecastDto): Forecast {
        val location = dto.location
        val currentWeather = mapToDomainWeather(CurrentWeatherDto(location, dto.current))

        val dailyForecasts = dto.forecast.forecastday.map { forecastDay ->
            mapToDomainDailyForecast(forecastDay)
        }

        val hourlyForecasts = dto.forecast.forecastday.flatMap { forecastDay ->
            forecastDay.hour.map { hourDto ->
                mapToDomainHourlyForecast(hourDto, forecastDay.date)
            }
        }

        return Forecast(
            cityName = location.name,
            region = location.region,
            country = location.country,
            currentWeather = currentWeather,
            dailyForecasts = dailyForecasts,
            hourlyForecasts = hourlyForecasts
        )
    }

    private fun mapToDomainDailyForecast(dto: ForecastDayDto): DailyForecast {
        val date = LocalDate.parse(dto.date, dateFormatter)

        return DailyForecast(
            date = date,
            maxTemp = dto.day.maxtempC,
            minTemp = dto.day.mintempC,
            avgTemp = dto.day.avgtempC,
            maxWind = dto.day.maxwindKph,
            totalPrecipitation = dto.day.totalprecipMm,
            avgHumidity = dto.day.avghumidity,
            chanceOfRain = dto.day.dailyChanceOfRain,
            chanceOfSnow = dto.day.dailyChanceOfSnow,
            condition = WeatherCondition(
                text = dto.day.condition.text,
                icon = dto.day.condition.icon,
                code = dto.day.condition.code
            ),
            sunrise = dto.astro.sunrise,
            sunset = dto.astro.sunset,
            moonPhase = dto.astro.moonPhase
        )
    }

    private fun mapToDomainHourlyForecast(dto: HourDto, dateString: String): HourlyForecast {
        val time = parseLocalDateTime(dto.time)

        return HourlyForecast(
            time = time,
            temperature = dto.tempC,
            feelsLike = dto.feelslikeC,
            condition = WeatherCondition(
                text = dto.condition.text,
                icon = dto.condition.icon,
                code = dto.condition.code
            ),
            windSpeed = dto.windKph,
            windDirection = dto.windDir,
            pressure = dto.pressureMb.toInt(),
            precipitation = dto.precipMm,
            humidity = dto.humidity,
            cloudCover = dto.cloud,
            chanceOfRain = dto.chanceOfRain,
            isDay = dto.isDay == 1
        )
    }

    fun mapToDomainWeather(entity: WeatherEntity): Weather {
        return Weather(
            id = entity.id,
            cityName = entity.cityName,
            region = entity.region,
            country = entity.country,
            temperature = entity.temperature,
            feelsLike = entity.feelsLike,
            humidity = entity.humidity,
            pressure = entity.pressure,
            windSpeed = entity.windSpeed,
            windDirection = entity.windDirection,
            windDirectionText = entity.windDirectionText,
            description = entity.description,
            icon = entity.icon,
            uv = entity.uv,
            cloudCover = entity.cloudCover,
            precipitation = entity.precipitation,
            visibility = entity.visibility,
            isDay = entity.isDay,
            timestamp = entity.timestamp,
            lastUpdated = entity.lastUpdated
        )
    }

    fun mapToDomainForecast(
        weatherEntity: WeatherEntity?,
        dailyEntities: List<DailyForecastEntity>,
        hourlyEntities: List<HourlyForecastEntity>
    ): Forecast {
        val currentWeather = weatherEntity?.let { mapToDomainWeather(it) }

        val dailyForecasts = dailyEntities.map { entity ->
            DailyForecast(
                date = entity.date,
                maxTemp = entity.maxTemp,
                minTemp = entity.minTemp,
                avgTemp = entity.avgTemp,
                maxWind = entity.maxWind,
                totalPrecipitation = entity.totalPrecipitation,
                avgHumidity = entity.avgHumidity,
                chanceOfRain = entity.chanceOfRain,
                chanceOfSnow = entity.chanceOfSnow,
                condition = WeatherCondition(
                    text = entity.conditionText,
                    icon = entity.conditionIcon,
                    code = entity.conditionCode
                ),
                sunrise = entity.sunrise,
                sunset = entity.sunset,
                moonPhase = entity.moonPhase
            )
        }

        val hourlyForecasts = hourlyEntities.map { entity ->
            HourlyForecast(
                time = entity.time,
                temperature = entity.temperature,
                feelsLike = entity.feelsLike,
                condition = WeatherCondition(
                    text = entity.conditionText,
                    icon = entity.conditionIcon,
                    code = entity.conditionCode
                ),
                windSpeed = entity.windSpeed,
                windDirection = entity.windDirection,
                pressure = entity.pressure,
                precipitation = entity.precipitation,
                humidity = entity.humidity,
                cloudCover = entity.cloudCover,
                chanceOfRain = entity.chanceOfRain,
                isDay = entity.isDay
            )
        }

        return Forecast(
            cityName = weatherEntity?.cityName ?: dailyEntities.firstOrNull()?.cityName ?: "",
            region = weatherEntity?.region ?: "",
            country = weatherEntity?.country ?: "",
            currentWeather = currentWeather,
            dailyForecasts = dailyForecasts,
            hourlyForecasts = hourlyForecasts
        )
    }

    fun mapToWeatherEntity(domain: Weather): WeatherEntity {
        return WeatherEntity(
            id = domain.id,
            cityName = domain.cityName,
            region = domain.region,
            country = domain.country,
            temperature = domain.temperature,
            feelsLike = domain.feelsLike,
            humidity = domain.humidity,
            pressure = domain.pressure,
            windSpeed = domain.windSpeed,
            windDirection = domain.windDirection,
            windDirectionText = domain.windDirectionText,
            description = domain.description,
            icon = domain.icon,
            uv = domain.uv,
            cloudCover = domain.cloudCover,
            precipitation = domain.precipitation,
            visibility = domain.visibility,
            isDay = domain.isDay,
            timestamp = domain.timestamp,
            lastUpdated = domain.lastUpdated
        )
    }

    fun mapToDailyForecastEntity(domain: DailyForecast, cityName: String): DailyForecastEntity {
        return DailyForecastEntity(
            cityName = cityName,
            date = domain.date,
            maxTemp = domain.maxTemp,
            minTemp = domain.minTemp,
            avgTemp = domain.avgTemp,
            maxWind = domain.maxWind,
            totalPrecipitation = domain.totalPrecipitation,
            avgHumidity = domain.avgHumidity,
            chanceOfRain = domain.chanceOfRain,
            chanceOfSnow = domain.chanceOfSnow,
            conditionText = domain.condition.text,
            conditionIcon = domain.condition.icon,
            conditionCode = domain.condition.code,
            sunrise = domain.sunrise,
            sunset = domain.sunset,
            moonPhase = domain.moonPhase
        )
    }

    fun mapToHourlyForecastEntity(domain: HourlyForecast, cityName: String): HourlyForecastEntity {
        return HourlyForecastEntity(
            cityName = cityName,
            forecastDate = domain.time.toLocalDate(),
            time = domain.time,
            temperature = domain.temperature,
            feelsLike = domain.feelsLike,
            conditionText = domain.condition.text,
            conditionIcon = domain.condition.icon,
            conditionCode = domain.condition.code,
            windSpeed = domain.windSpeed,
            windDirection = domain.windDirection,
            pressure = domain.pressure,
            precipitation = domain.precipitation,
            humidity = domain.humidity,
            cloudCover = domain.cloudCover,
            chanceOfRain = domain.chanceOfRain,
            isDay = domain.isDay
        )
    }


    private fun parseLocalDateTime(dateTimeString: String): LocalDateTime {
        return try {
            LocalDateTime.parse(dateTimeString, dateTimeFormatter)
        } catch (e: DateTimeParseException) {
            try {
                val date = LocalDate.parse(dateTimeString.split(" ")[0], dateFormatter)
                date.atStartOfDay()
            } catch (e: Exception) {
                LocalDateTime.now()
            }
        }
    }
}