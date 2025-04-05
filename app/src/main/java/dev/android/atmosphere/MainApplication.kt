package dev.android.atmosphere

import android.app.Application
import dev.android.atmosphere.di.appModule
import dev.android.atmosphere.di.dataModule
import dev.android.atmosphere.di.domainModule
import dev.android.atmosphere.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MainApplication)
            modules(listOf(
                appModule,
                dataModule,
                domainModule,
                presentationModule
            ))
        }
    }
}