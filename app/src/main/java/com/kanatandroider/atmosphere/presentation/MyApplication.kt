package com.kanatandroider.atmosphere.presentation

import android.app.Application
import com.kanatandroider.atmosphere.di.DaggerApplicationComponent

class MyApplication : Application() {


//    @Inject
//    lateinit var workerFactory: UpdateDataFactory


    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

    override fun onCreate() {
        component.inject(this)
        super.onCreate()
    }
//    override val workManagerConfiguration: androidx.work.Configuration
//         get() = androidx.work.Configuration.Builder()
//            .setWorkerFactory(workerFactory)
//            .build()
}