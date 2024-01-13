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

}