package dev.android.atmosphere.presentation.screens.permission

import android.Manifest
import android.content.Context
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import dev.android.atmosphere.R
import dev.android.atmosphere.presentation.navigation.Screen
import dev.android.atmosphere.presentation.utils.openAppSettings
import dev.android.atmosphere.presentation.utils.openLocationSettings
import org.koin.androidx.compose.koinViewModel


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

    HandlePermissionEffects(
        permissionState = permissionState,
        permissionGranted = permissionGranted,
        isLocationServiceEnabled = isLocationServiceEnabled,
        error = error,
        viewModel = viewModel,
        navController = navController
    )

    DisplayLocationPermissionContent(
        isLoading = isLoading,
        isLocationServiceEnabled = isLocationServiceEnabled,
        error = error,
        permissionGranted = permissionGranted,
        viewModel = viewModel,
        permissionState = permissionState,
        navController = navController,
        context = context
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun HandlePermissionEffects(
    permissionState: PermissionState,
    permissionGranted: Boolean,
    isLocationServiceEnabled: Boolean,
    error: String?,
    viewModel: LocationPermissionViewModel,
    navController: NavController
) {
    LaunchedEffect(key1 = permissionState.status.isGranted) {
        viewModel.onPermissionResult(permissionState.status.isGranted)
    }

    LaunchedEffect(key1 = permissionGranted) {
        if (permissionGranted && isLocationServiceEnabled && error == null) {
            navController.navigate(Screen.Weather.route) {
                popUpTo(Screen.LocationPermission.route) {
                    inclusive = true
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun DisplayLocationPermissionContent(
    isLoading: Boolean,
    isLocationServiceEnabled: Boolean,
    error: String?,
    permissionGranted: Boolean,
    viewModel: LocationPermissionViewModel,
    permissionState: PermissionState,
    navController: NavController,
    context: Context
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = stringResource(id = R.string.location_icon_description),
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 24.dp)
        )

        Text(
            text = stringResource(id = R.string.location_permission_title),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = stringResource(id = R.string.location_permission_description),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        when {
            isLoading -> LoadingIndicator()
            !isLocationServiceEnabled -> ShowLocationServiceDisabled(context)
            error != null -> ShowErrorAndRetry(
                error = error,
                permissionGranted = permissionGranted,
                viewModel = viewModel,
                permissionState = permissionState,
                onRetry = { viewModel.checkLocationService() }
            )

            !permissionState.status.isGranted -> HandlePermissionNotGranted(
                permissionState = permissionState,
                navController = navController,
                context = context
            )
        }
    }
}

@Composable
private fun LoadingIndicator() {
    CircularProgressIndicator(
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
private fun ShowLocationServiceDisabled(context: Context) {
    Text(
        text = stringResource(id = R.string.location_service_disabled),
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier.padding(bottom = 16.dp)
    )

    Button(onClick = { openLocationSettings(context) }) {
        Text(text = stringResource(id = R.string.open_location_settings))
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun ShowErrorAndRetry(
    error: String?,
    permissionGranted: Boolean,
    viewModel: LocationPermissionViewModel,
    permissionState: PermissionState,
    onRetry: () -> Unit
) {
    Text(
        text = error ?: stringResource(id = R.string.generic_error),
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(bottom = 16.dp)
    )

    Button(
        onClick = {
            if (permissionGranted) {
                viewModel.checkLocationService()
            } else {
                permissionState.launchPermissionRequest()
            }
        }
    ) {
        Text(
            text = if (permissionGranted) stringResource(id = R.string.check_location_settings) else stringResource(id = R.string.request_permission)
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun HandlePermissionNotGranted(
    permissionState: PermissionState,
    navController: NavController,
    context: Context
) {
    if (permissionState.status.shouldShowRationale) {
        LocationPermissionRationaleText()
    }

    DisplayPermissionButtons(
        permissionState = permissionState,
        navController = navController,
        context = context
    )
}

@Composable
private fun LocationPermissionRationaleText() {
    Text(
        text = stringResource(id = R.string.location_permission_rationale),
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun DisplayPermissionButtons(
    permissionState: PermissionState,
    navController: NavController,
    context: Context
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        Button(
            onClick = {
                permissionState.launchPermissionRequest()
            }
        ) {
            Text(text = stringResource(id = R.string.allow_permission))
        }

        OutlinedButton(
            onClick = {
                navController.navigate(Screen.CitySelection.route) {
                    popUpTo(Screen.LocationPermission.route) {
                        inclusive = true
                    }
                }
            }
        ) {
            Text(text = stringResource(id = R.string.search_manually))
        }
    }

    TextButton(
        onClick = {
            openAppSettings(context)
        },
        modifier = Modifier.padding(top = 8.dp)
    ) {
        Text(text = stringResource(id = R.string.open_app_settings))
    }
}