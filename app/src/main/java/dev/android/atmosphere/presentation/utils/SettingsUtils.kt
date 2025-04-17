package dev.android.atmosphere.presentation.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

private const val PACKAGE_URI_SCHEME = "package"

fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts(PACKAGE_URI_SCHEME, context.packageName, null)
    }
    context.startActivity(intent)
}

fun openLocationSettings(context: Context) {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    context.startActivity(intent)
}