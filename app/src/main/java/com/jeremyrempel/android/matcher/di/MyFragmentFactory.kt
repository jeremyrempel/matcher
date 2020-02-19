package com.jeremyrempel.android.matcher.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.manager.SupportRequestManagerFragment
import com.jeremyrempel.android.matcher.features.search.view.MatchPerctFragment
import com.jeremyrempel.android.matcher.features.search.view.SearchMainFragment
import com.jeremyrempel.android.matcher.features.search.view.SpecialBlendFragment
import javax.inject.Inject
import javax.inject.Provider

/**
 * Alternative could use keys to map. Similar amount of boilerplate
 */
class MyFragmentFactory @Inject constructor(
    private val mainFragProvider: Provider<SearchMainFragment>,
    private val specialBlendProvider: Provider<SpecialBlendFragment>,
    private val matchProvider: Provider<MatchPerctFragment>
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            SearchMainFragment::class.java.canonicalName -> mainFragProvider.get()
            SpecialBlendFragment::class.java.canonicalName -> specialBlendProvider.get()
            MatchPerctFragment::class.java.canonicalName -> matchProvider.get()
            SupportRequestManagerFragment::class.java.canonicalName -> SupportRequestManagerFragment()
            else -> TODO("Missing dagger provider, add $className to MyFragmentFactory")
        }
    }
}
