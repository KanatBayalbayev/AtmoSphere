package com.kanatandroider.atmosphere.di

import android.app.Application
import com.kanatandroider.atmosphere.presentation.activities.CurrentWeatherActivity
import com.kanatandroider.atmosphere.presentation.activities.LocationActivity
import com.kanatandroider.atmosphere.presentation.activities.MyApplication
import com.kanatandroider.atmosphere.presentation.activities.NextDayDetailsActivity
import com.kanatandroider.atmosphere.presentation.activities.NextDaysActivity
import com.kanatandroider.atmosphere.presentation.activities.WelcomeActivity
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {
    fun inject(myApplication: MyApplication)
    fun inject(locationActivity: LocationActivity)
    fun inject(welcomeActivity: WelcomeActivity)
    fun inject(currentWeatherActivity: CurrentWeatherActivity)
    fun inject(nextDaysActivity: NextDaysActivity)
    fun inject(nextDayDetailsActivity: NextDayDetailsActivity)




    @Component.Factory
    interface ComponentFactory{
        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}