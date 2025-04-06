package dev.android.atmosphere.presentation.screens.permission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.android.atmosphere.R
import dev.android.atmosphere.presentation.navigation.Screen
import org.koin.androidx.compose.koinViewModel
import android.provider.Settings
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionScreen(
    navController: NavController,
    viewModel: LocationPermissionViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val permissionState = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION
    )
    val permissionGranted by viewModel.permissionGranted.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val isLocationServiceEnabled by viewModel.isLocationServiceEnabled.collectAsState()

    LaunchedEffect(key1 = permissionState.status.isGranted) {
        viewModel.onPermissionResult(permissionState.status.isGranted)
    }

    LaunchedEffect(key1 = permissionGranted) {
        if (permissionGranted && isLocationServiceEnabled && error == null) {
            // Если все проверки пройдены, переходим к главному экрану
            navController.navigate(Screen.Weather.route) {
                popUpTo(Screen.LocationPermission.route) {
                    inclusive = true
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background), // Нужно создать или использовать существующую иконку
            contentDescription = "Иконка местоположения",
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 24.dp)
        )

        Text(
            text = "Разрешение на местоположение",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Для получения точного прогноза погоды в вашем районе нам необходимо разрешение на доступ к вашему местоположению.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.padding(16.dp)
            )
        } else if (!isLocationServiceEnabled) {
            // Службы геолокации отключены
            Text(
                text = "Службы геолокации отключены. Включите GPS в настройках устройства для точного определения местоположения.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Button(
                onClick = {
                    openLocationSettings(context)
                }
            ) {
                Text(text = "Открыть настройки геолокации")
            }
        } else if (error != null) {
            // Ошибка получения местоположения
            Text(
                text = error ?: "Произошла ошибка",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Button(
                onClick = {
                    if (permissionGranted) {
                        // Проверяем настройки местоположения снова
                        viewModel.checkLocationService()
                    } else {
                        // Запрашиваем разрешение снова
                        permissionState.launchPermissionRequest()
                    }
                }
            ) {
                Text(
                    text = if (permissionGranted) "Проверить настройки геолокации" else "Запросить разрешение"
                )
            }
        } else if (!permissionState.status.isGranted) {
            if (permissionState.status.shouldShowRationale) {
                // Показать объяснение, почему нужно разрешение
                Text(
                    text = "Для корректной работы приложения необходим доступ к местоположению. Пожалуйста, предоставьте разрешение в настройках.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                Button(
                    onClick = {
                        permissionState.launchPermissionRequest()
                    }
                ) {
                    Text(text = "Разрешить")
                }

                OutlinedButton(
                    onClick = {
                        // Переход к экрану погоды без разрешения
                        navController.navigate(Screen.Weather.route) {
                            popUpTo(Screen.LocationPermission.route) {
                                inclusive = true
                            }
                        }
                    }
                ) {
                    Text(text = "Позже")
                }
            }

            TextButton(
                onClick = {
                    openAppSettings(context)
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(text = "Открыть настройки приложения")
            }
        }
    }
}

private fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}

private fun openLocationSettings(context: Context) {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    context.startActivity(intent)
}