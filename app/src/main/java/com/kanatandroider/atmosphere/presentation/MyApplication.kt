package com.kanatandroider.atmosphere.presentation

import android.app.Application
import com.kanatandroider.atmosphere.di.DaggerApplicationComponent

class MyApplication : Application() {


    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}