package com.kanatandroider.atmosphere.di

import android.app.Application
import com.kanatandroider.atmosphere.presentation.MainActivity
import com.kanatandroider.atmosphere.presentation.MyApplication
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)

    fun inject(myApplication: MyApplication)


    @Component.Factory
    interface ComponentFactory{
        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}