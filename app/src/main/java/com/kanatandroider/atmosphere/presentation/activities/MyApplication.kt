package com.kanatandroider.atmosphere.presentation.activities

import android.app.Application
import com.kanatandroider.atmosphere.di.DaggerApplicationComponent

class MyApplication : Application() {


    val component by lazy {
        DaggerApplicationComponent.factory().create(this)

    }

    override fun onCreate() {
        component.inject(this)
        super.onCreate()
    }
}