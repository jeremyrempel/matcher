package com.jeremyrempel.android.matcher.features.search.api

import io.reactivex.Single
import retrofit2.http.GET

interface MatchService {
    @GET("matchSample.json")
    fun getMatch(): Single<MatchResponse>
}
