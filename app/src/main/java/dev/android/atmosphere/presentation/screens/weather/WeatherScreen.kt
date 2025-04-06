package dev.android.atmosphere.presentation.screens.weather

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.android.atmosphere.domain.model.Forecast
import dev.android.atmosphere.domain.model.Weather
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

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
            TopAppBar(
                title = { Text("Прогноз погоды") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = { viewModel.refreshWeatherData() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Обновить",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        },
        bottomBar = {
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
            // Состояние загрузки
            if (weatherState is WeatherViewModel.WeatherState.Loading || forecastState is WeatherViewModel.ForecastState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Состояние ошибки
            if (weatherState is WeatherViewModel.WeatherState.Error && forecastState is WeatherViewModel.ForecastState.Error) {
                ErrorView(
                    message = (weatherState as WeatherViewModel.WeatherState.Error).message,
                    onRetry = { viewModel.refreshWeatherData() }
                )
            }

            // Состояние успеха - отображаем данные
            if (weatherState is WeatherViewModel.WeatherState.Success) {
                val weather = (weatherState as WeatherViewModel.WeatherState.Success).weather
                val forecast = if (forecastState is WeatherViewModel.ForecastState.Success) {
                    (forecastState as WeatherViewModel.ForecastState.Success).forecast
                } else null

//                AnimatedVisibility(
//                    visible = true,
//                    enter = fadeIn(),
//                    exit = fadeOut()
//                ) {
//                    WeatherContent(
//                        weather = weather,
//                        forecast = forecast
//                    )
//                }
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

//@Composable
//fun WeatherContent(
//    weather: Weather,
//    forecast: Forecast?
//) {
//    val now = LocalDateTime.now()
//    val dateFormatter = DateTimeFormatter.ofPattern("E, d MMMM", Locale("ru"))
//    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
//
//    LazyColumn(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        // Информация о местоположении и дате
//        item {
//            Text(
//                text = "${weather.cityName}, ${weather.country}",
//                style = MaterialTheme.typography.titleLarge,
//                modifier = Modifier.padding(top = 16.dp)
//            )
//
//            Text(
//                text = now.format(dateFormatter),
//                style = MaterialTheme.typography.titleSmall,
//                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
//                modifier = Modifier.padding(bottom = 24.dp)
//            )
//        }
//
//        // Текущая погода
//        item {
//            CurrentWeatherCard(weather = weather)
//        }
//
//        // Почасовой прогноз
//        if (forecast != null && forecast.hourlyForecasts.isNotEmpty()) {
//            item {
//                Text(
//                    text = "Почасовой прогноз",
//                    style = MaterialTheme.typography.titleMedium,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 16.dp),
//                    textAlign = TextAlign.Start
//                )
//
//                HourlyForecastList(forecast.hourlyForecasts)
//            }
//        }
//
//        // Прогноз на несколько дней
//        if (forecast != null && forecast.dailyForecasts.isNotEmpty()) {
//            item {
//                Text(
//                    text = "Прогноз на неделю",
//                    style = MaterialTheme.typography.titleMedium,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 16.dp),
//                    textAlign = TextAlign.Start
//                )
//
//                DailyForecastList(forecast.dailyForecasts)
//            }
//        }
//    }
//}
//
//@Composable
//fun CurrentWeatherCard(weather: Weather) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            // Иконка и температура
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.padding(bottom = 16.dp)
//            ) {
//                AsyncImage(
//                    model = WeatherIconUtil.getHighQualityIconUrl(weather.icon),
//                    contentDescription = weather.description,
//                    modifier = Modifier.size(100.dp)
//                )
//
//                Column(
//                    horizontalAlignment = Alignment.Start
//                ) {
//                    Text(
//                        text = "${weather.temperature.toInt()}°C",
//                        style = MaterialTheme.typography.headlineLarge
//                    )
//
//                    Text(
//                        text = weather.description,
//                        style = MaterialTheme.typography.titleMedium
//                    )
//
//                    Text(
//                        text = "Ощущается как: ${weather.feelsLike.toInt()}°C",
//                        style = MaterialTheme.typography.bodyMedium,
//                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
//                    )
//                }
//            }
//
//            Divider(modifier = Modifier.padding(vertical = 8.dp))
//
//
//        }
//    }
//    }