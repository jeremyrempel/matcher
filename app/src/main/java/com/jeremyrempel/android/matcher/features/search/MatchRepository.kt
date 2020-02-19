package com.jeremyrempel.android.matcher.features.search

import com.jeremyrempel.android.matcher.features.search.api.MatchResponse
import com.jeremyrempel.android.matcher.features.search.api.MatchService
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Return a list of data, the first time fetch from network
 * Merge the first list with a second list to return a modified dataset
 *
 * When second data set changes emit new result
 */
@Singleton
class MatchRepository @Inject constructor(
    service: MatchService
) {
    // todo persist to db
    private val userSelections = BehaviorSubject.createDefault(setOf<String>())
    private val response = service.getMatch().toObservable().cacheWithInitialCapacity(1)

    fun selectCard(card: UserCard) {
        val oldSet = userSelections.value ?: setOf()
        val newSet = if (oldSet.contains(card.name)) {
            oldSet.filter { it != card.name }
        } else {
            oldSet + card.name
        }
        userSelections.onNext(newSet.toSet())
    }

    /**
     * Get all results and combine with user selections
     */
    fun getSpecialBlend(): Observable<List<UserCard>> {
        return Observable.combineLatest(response, userSelections,
            BiFunction { a: MatchResponse, b: Set<String> ->
                a.toUserCard()
                    .map { it.copy(isSelected = b.contains(it.name)) }
            }
        )
    }

    /**
     * Get all results, combine with user selections and show only first 6 with highest match %
     */
    fun getMatchPerct(numResults: Int = 6): Observable<List<UserCard>> {
        return Observable.combineLatest(response, userSelections,
            BiFunction { a: MatchResponse, b: Set<String> ->
                a.toUserCard()
                    .map { it.copy(isSelected = b.contains(it.name)) }
                    .filter { it.isSelected }
                    .sortedByDescending { it.matchPercentage }
                    .take(numResults)
            }
        )
    }

    private fun MatchResponse.toUserCard(): List<UserCard> {
        return this.data?.map {
            UserCard(
                it.username,
                "${it.age} • ${it.city_name}, ${it.state_name}",
                it.match / 100,
                it.photo.full_paths.large
            )
        } ?: listOf()
    }
}
