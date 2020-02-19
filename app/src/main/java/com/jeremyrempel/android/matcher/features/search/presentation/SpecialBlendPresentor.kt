package com.jeremyrempel.android.matcher.features.search.presentation

import android.annotation.SuppressLint
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.jeremyrempel.android.matcher.Lce
import com.jeremyrempel.android.matcher.features.search.MatchRepository
import com.jeremyrempel.android.matcher.features.search.UserCard
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class SpecialBlendPresentor @Inject constructor(
    private val repository: MatchRepository,
    @Named("compute")
    private val schedulerCompute: Scheduler,
    @Named("main")
    private val schedulerMain: Scheduler
) : LifecycleObserver {

    private val _viewState = BehaviorSubject.create<Lce<List<UserCard>>>()
    private val disposable = CompositeDisposable()

    /**
     * Subscribe to ViewState and assign observable
     */
    fun subscribeViewState(lifecycle: Lifecycle): Observable<Lce<List<UserCard>>> {
        lifecycle.addObserver(this)
        return _viewState.hide()
    }

    @SuppressLint("CheckResult")
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun load() {
        _viewState.onNext(Lce.Loading())

        repository
            .getSpecialBlend()
            .map { Lce.Content(it) }
            .subscribeOn(schedulerCompute)
            .observeOn(schedulerMain)
            .subscribe(_viewState::onNext, _viewState::onError)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun clear() {
        Timber.d("----- onDestroy clearing rx disposable")
        disposable.clear()
    }

    fun onSelected(card: UserCard) = repository.selectCard(card)
}
