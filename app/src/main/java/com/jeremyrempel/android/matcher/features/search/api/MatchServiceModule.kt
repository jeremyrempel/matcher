package com.jeremyrempel.android.matcher.features.search.api

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
object MatchServiceModule {

    @Provides
    @Singleton
    fun providesBlockChainService(retrofit: Retrofit): MatchService {
        return retrofit.create(MatchService::class.java)
    }
}
