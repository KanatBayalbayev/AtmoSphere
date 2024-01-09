package com.kanatandroider.atmosphere.presentation

import android.app.Application
import android.content.res.Configuration
import com.kanatandroider.atmosphere.data.workmanager.UpdateDataFactory
import com.kanatandroider.atmosphere.di.DaggerApplicationComponent
import javax.inject.Inject

class MyApplication : Application(), androidx.work.Configuration.Provider {


    @Inject
    lateinit var workerFactory: UpdateDataFactory


    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

    override fun onCreate() {
        component.inject(this)
        super.onCreate()
    }
    override val workManagerConfiguration: androidx.work.Configuration
         get() = androidx.work.Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}