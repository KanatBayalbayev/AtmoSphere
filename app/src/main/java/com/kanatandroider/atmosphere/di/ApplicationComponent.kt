package com.kanatandroider.atmosphere.di

import android.app.Application
import com.kanatandroider.atmosphere.presentation.MainActivity
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)


    @Component.Factory
    interface ComponentFactory{
        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}