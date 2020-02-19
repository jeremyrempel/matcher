package com.jeremyrempel.android.matcher

import android.app.Application
import com.jeremyrempel.android.matcher.di.AppComponent
import com.jeremyrempel.android.matcher.di.AppModule
import com.jeremyrempel.android.matcher.di.DaggerAppComponent
import timber.log.Timber

class MyApplication : Application() {

    lateinit var dagger: AppComponent

    override fun onCreate() {
        super.onCreate()

        dagger = DaggerAppComponent
            .builder()
            .appModule(AppModule(applicationContext))
            .build()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
