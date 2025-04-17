package dev.android.atmosphere.presentation.screens.weather

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.android.atmosphere.domain.model.HourlyForecast
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = koinViewModel()
) {
    val weatherState by viewModel.weatherState.collectAsState()
    val forecastState by viewModel.forecastState.collectAsState()
    val locationState by viewModel.locationState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(key1 = weatherState) {
        if (weatherState is WeatherViewModel.WeatherState.Error) {
            snackbarHostState.showSnackbar((weatherState as WeatherViewModel.WeatherState.Error).message)
        }
    }

    Scaffold(
        topBar = {
            SearchBar(
                query = searchQuery,
                onQueryChange = { viewModel.searchCity(it) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Show weather display when data is successfully loaded
            if (weatherState is WeatherViewModel.WeatherState.Success) {
                val weather = (weatherState as WeatherViewModel.WeatherState.Success).weather

                // Display main weather information
                WeatherDisplay(
                    cityName = weather.cityName,
                    iconCode = weather.icon,
                    temperature = weather.temperature,
                    feelsLike = weather.feelsLike,
                    description = weather.description,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }

            // Show weather details row when forecast data is successfully loaded
            if (forecastState is WeatherViewModel.ForecastState.Success) {
                val forecast = (forecastState as WeatherViewModel.ForecastState.Success).forecast

                // Get the current weather from forecast if available
                val currentWeather = forecast.currentWeather

                // Use data from the first daily forecast if available, otherwise fallback to defaults
                val dailyForecast = forecast.dailyForecasts.firstOrNull()

                // Position the details row below the main weather display
                WeatherDetailsRow(
                    chanceOfRain = dailyForecast?.chanceOfRain ?: 0,
                    windSpeed = currentWeather?.windSpeed ?: 0.0,
                    humidity = currentWeather?.humidity ?: 0,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 240.dp) // Position it below the weather display
                )

                // Show hourly forecast below the weather details
                HourlyForecastRow(
                    hourlyForecasts = forecast.hourlyForecasts,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 340.dp) // Position it below the weather details
                )
            }

            if (weatherState is WeatherViewModel.WeatherState.Loading || forecastState is WeatherViewModel.ForecastState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            if (weatherState is WeatherViewModel.WeatherState.Error && forecastState is WeatherViewModel.ForecastState.Error) {
                ErrorView(
                    message = (weatherState as WeatherViewModel.WeatherState.Error).message,
                    onRetry = { viewModel.refreshWeatherData() }
                )
            }

            if (locationState is WeatherViewModel.LocationState.Success) {
                val location = (locationState as WeatherViewModel.LocationState.Success).location
                Log.d("WeatherScreen", "location: $location")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    var active by remember { mutableStateOf(false) }

    SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = {
            active = false
            onQueryChange(it)
        },
        active = active,
        onActiveChange = { active = it },
        placeholder = { Text("Поиск города...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Поиск") },
        modifier = Modifier.fillMaxWidth()
    ) {
        // Поисковые подсказки можно добавить позже
    }
}

@Composable
fun ErrorView(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CloudOff,
            contentDescription = "Ошибка",
            modifier = Modifier
                .size(80.dp)
                .padding(bottom = 16.dp),
            tint = MaterialTheme.colorScheme.error
        )

        Text(
            text = message,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(onClick = onRetry) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Повторить")
        }
    }
}

@Composable
fun WeatherDisplay(
    cityName: String,
    iconCode: String,
    temperature: Double,
    feelsLike: Double,
    description: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // City name
            Text(
                text = cityName,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Icon and temperatures container
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(8.dp)
            ) {
                // Weather icon
                WeatherIcon(iconCode = iconCode)

                Spacer(modifier = Modifier.height(8.dp))

                // Temperature
                Text(
                    text = "${temperature.toInt()}°",
                    style = MaterialTheme.typography.displayMedium
                )

                // Feels like temperature
                Text(
                    text = "Ощущается как ${feelsLike.toInt()}°",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Weather description
                Text(
                    text = description.capitalize(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun WeatherIcon(iconCode: String) {
    // This is a simplified version, you may want to map icon codes to your own icons
    // or load them from a remote source
    val iconResource = when {
        iconCode.contains("01") -> Icons.Default.WbSunny // Clear sky
        iconCode.contains("02") -> Icons.Default.Cloud // Few clouds
        iconCode.contains("03") || iconCode.contains("04") -> Icons.Default.Cloud // Clouds
        iconCode.contains("09") || iconCode.contains("10") -> Icons.Default.Grain // Rain
        iconCode.contains("11") -> Icons.Default.Thunderstorm // Thunderstorm
        iconCode.contains("13") -> Icons.Default.AcUnit // Snow
        iconCode.contains("50") -> Icons.Default.Cloud // Mist/Fog
        else -> Icons.Default.Cloud // Default
    }

    Icon(
        imageVector = iconResource,
        contentDescription = "Weather icon",
        modifier = Modifier.size(64.dp),
        tint = MaterialTheme.colorScheme.primary
    )
}

// Extension function to capitalize the first letter of a string
fun String.capitalize(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

@Composable
fun WeatherDetailsRow(
    chanceOfRain: Int,
    windSpeed: Double,
    humidity: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 50.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Chance of Rain
        WeatherDetailItem(
            icon = Icons.Default.WaterDrop,
            value = "$chanceOfRain%",
            label = "Вероятность дождя"
        )

        // Wind Speed
        WeatherDetailItem(
            icon = Icons.Default.Air,
            value = "${windSpeed.toInt()} м/с",
            label = "Скорость ветра"
        )

        // Humidity
        WeatherDetailItem(
            icon = Icons.Default.Opacity,
            value = "$humidity%",
            label = "Влажность"
        )
    }
}

@Composable
fun WeatherDetailItem(
    icon: ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun HourlyForecastRow(
    hourlyForecasts: List<HourlyForecast>,
    modifier: Modifier = Modifier
) {
    // Get current time for comparison
    val currentTime = LocalDateTime.now()
    val currentDate = currentTime.toLocalDate()
    val tomorrowDate = currentDate.plusDays(1)

    // Filter forecasts to show current and next day up to 23:00
    val filteredForecasts = hourlyForecasts.filter { forecast ->
        // Get the date of this forecast
        val forecastDate = forecast.time.toLocalDate()

        // Check if forecast is for today or tomorrow
        val isCurrentOrNextDay = forecastDate.isEqual(currentDate) || forecastDate.isEqual(tomorrowDate)

        // Check if forecast time is not after 23:00
        val isBeforeEndOfDay = forecast.time.hour <= 23

        // For today's forecasts, ensure we only get from current hour onwards
        val isValidTodayForecast = if (forecastDate.isEqual(currentDate)) {
            !forecast.time.isBefore(currentTime.withMinute(0).withSecond(0).withNano(0))
        } else {
            true // For tomorrow, we show all hours
        }

        isCurrentOrNextDay && isBeforeEndOfDay && isValidTodayForecast
    }

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 50.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(filteredForecasts) { forecast ->
            HourlyForecastItem(
                time = forecast.time,
                temperature = forecast.temperature,
                iconCode = forecast.condition.icon,
                isCurrentHour = isCurrentHour(forecast.time, currentTime)
            )
        }
    }
}

@Composable
fun HourlyForecastItem(
    time: LocalDateTime,
    temperature: Double,
    iconCode: String,
    isCurrentHour: Boolean,
    modifier: Modifier = Modifier
) {
    val currentDate = LocalDateTime.now().toLocalDate()
    val isTomorrow = time.toLocalDate().isAfter(currentDate)
    val isFirstHourOfTomorrow = isTomorrow && time.hour == 0

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(8.dp)
            .width(64.dp)
    ) {
        // Reserve space for "Завтра" label to keep all time texts aligned
        if (isFirstHourOfTomorrow) {
            Text(
                text = "Завтра",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        } else {
            // Empty spacer with height equivalent to the "Завтра" text
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Time display - show "Сейчас" for current hour, otherwise show formatted time
        Text(
            text = if (isCurrentHour) "Сейчас" else formatTime(time),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isCurrentHour) FontWeight.Bold else FontWeight.Normal,
            color = if (isCurrentHour) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Weather icon
        WeatherIconSmall(iconCode = iconCode)

        Spacer(modifier = Modifier.height(4.dp))

        // Temperature
        Text(
            text = "${temperature.toInt()}°",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun WeatherIconSmall(iconCode: String) {
    // This is a simplified version, similar to the larger WeatherIcon but smaller
    val iconResource = when {
        iconCode.contains("01") -> Icons.Default.WbSunny // Clear sky
        iconCode.contains("02") -> Icons.Default.Cloud // Few clouds
        iconCode.contains("03") || iconCode.contains("04") -> Icons.Default.Cloud // Clouds
        iconCode.contains("09") || iconCode.contains("10") -> Icons.Default.Grain // Rain
        iconCode.contains("11") -> Icons.Default.Thunderstorm // Thunderstorm
        iconCode.contains("13") -> Icons.Default.AcUnit // Snow
        iconCode.contains("50") -> Icons.Default.Cloud // Mist/Fog
        else -> Icons.Default.Cloud // Default
    }

    Icon(
        imageVector = iconResource,
        contentDescription = "Weather icon",
        modifier = Modifier.size(32.dp),
        tint = MaterialTheme.colorScheme.primary
    )
}

// Helper function to format time from LocalDateTime
private fun formatTime(time: LocalDateTime): String {
    val hour = time.hour
    // Format hour with leading zero if needed
    val formattedHour = if (hour < 10) "0$hour" else "$hour"
    return "$formattedHour:00"
}

// Helper function to determine if a given time is in the current hour
private fun isCurrentHour(forecastTime: LocalDateTime, currentTime: LocalDateTime): Boolean {
    return forecastTime.year == currentTime.year &&
            forecastTime.month == currentTime.month &&
            forecastTime.dayOfMonth == currentTime.dayOfMonth &&
            forecastTime.hour == currentTime.hour
}