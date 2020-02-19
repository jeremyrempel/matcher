package com.jeremyrempel.android.matcher.features.search.view

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jeremyrempel.android.matcher.Lce
import com.jeremyrempel.android.matcher.R
import com.jeremyrempel.android.matcher.features.search.UserCard
import com.jeremyrempel.android.matcher.features.search.presentation.MatchPerctPresentor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

class MatchPerctFragment @Inject constructor(
    private val presenter: MatchPerctPresentor
) : Fragment(R.layout.fragment_list) {

    private var listAdapter: UserCardListAdapter? = null
    private val disposables = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdapter =
            UserCardListAdapter {
                Timber.d("onclick: $it")
                presenter.onSelected(it)
            }
        view.findViewById<RecyclerView>(R.id.recycler).apply {
            setHasFixedSize(true)
            val layout = GridLayoutManager(
                context,
                GRID_SPAN_CNT
            )

            layoutManager = layout
            adapter = this@MatchPerctFragment.listAdapter
        }
    }

    override fun onStart() {
        super.onStart()

        disposables.add(
            presenter
                .subscribeViewState(lifecycle)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { Timber.d("----- onNext VS $it") }
                .subscribe(
                    ::render
                ) {
                    Timber.w(it, "Error rendering view state. This shouldn't happen :(")
                }
        )
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
    }

    private fun showData(data: List<UserCard>) {
        listAdapter!!.submitList(data)
    }

    private fun render(viewState: Lce<List<UserCard>>) {

        val view = requireView()
        val loadingView = view.findViewById<View>(R.id.view_loading)
        val errorView: TextView = view.findViewById(R.id.view_error)
        val contentView = view.findViewById<View>(R.id.recycler)

        when (viewState) {
            is Lce.Loading -> {
                loadingView.visibility = View.VISIBLE
                errorView.visibility = View.GONE
                contentView.visibility = View.GONE
            }
            is Lce.Error -> {
                loadingView.visibility = View.GONE
                errorView.visibility = View.VISIBLE
                contentView.visibility = View.GONE

                errorView.text = viewState.errorMsg
            }
            is Lce.Content -> {
                loadingView.visibility = View.GONE
                errorView.visibility = View.GONE
                contentView.visibility = View.VISIBLE

                showData(viewState.result)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listAdapter = null
    }

    companion object {
        const val GRID_SPAN_CNT = 2
    }
}
