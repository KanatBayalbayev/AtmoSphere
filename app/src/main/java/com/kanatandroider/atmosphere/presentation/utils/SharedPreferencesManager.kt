package com.kanatandroider.atmosphere.presentation.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(context: Context) {
    private val prefs = context.getSharedPreferences(
        "com.kanatandroider.atmosphere.PREFS",
        Context.MODE_PRIVATE
    )

    fun saveString(key: String, value: String) {
        with(prefs.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun getString(key: String, defaultValue: String): String? {
        return prefs.getString(key, defaultValue)
    }

    fun saveLatitude(key: String, value: String) {
        with(prefs.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun getLatitude(key: String, defaultValue: String): String? {
        return prefs.getString(key, defaultValue)
    }

    fun saveLongitude(key: String, value: String) {
        with(prefs.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun getLongitude(key: String, defaultValue: String): String? {
        return prefs.getString(key, defaultValue)
    }

    fun saveFinishedViewPagerContainerState(key: String, value: Boolean) {
        with(prefs.edit()) {
            putBoolean(key, value)
            apply()
        }
    }

    fun getFinishedViewPagerContainerState(key: String, defaultValue: Boolean): Boolean {
        return prefs.getBoolean(key, defaultValue)
    }



}