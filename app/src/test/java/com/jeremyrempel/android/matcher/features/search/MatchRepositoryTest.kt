package com.jeremyrempel.android.matcher.features.search

import com.jeremyrempel.android.matcher.features.search.api.MatchResponse
import com.jeremyrempel.android.matcher.features.search.api.MatchService
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Test

class MatchRepositoryTest {

    @Test
    fun `test get special blend`() {
        // setup
        val repo = MatchRepository(FakeService)

        // test
        val response = repo.getSpecialBlend().test()

        // verify
        assertEquals(1, response.valueCount())
    }
}

object FakeService : MatchService {
    val response = MatchResponse(
        listOf(
            MatchResponse.Data(
                8700,
                "username",
                30,
                "Brooklyn",
                "New York",
                MatchResponse.Photo(
                    MatchResponse.PhotoPaths("http://imageurl.com/1.jpg")
                )
            )
        )
    )

    override fun getMatch(): Single<MatchResponse> {
        return Single.just(response)
    }
}