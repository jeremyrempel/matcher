package com.jeremyrempel.android.matcher.di

import com.jeremyrempel.android.matcher.features.search.SearchFeatureModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, AppModule::class, SearchFeatureModule::class])
interface AppComponent {
    @Singleton
    fun fragFactory(): MyFragmentFactory
}
