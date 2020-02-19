package com.jeremyrempel.android.matcher.features.search.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.jeremyrempel.android.matcher.R
import javax.inject.Inject

class SearchMainFragment @Inject constructor() : Fragment(R.layout.fragment_main_tabs) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabs(view)
    }

    private fun setupTabs(view: View) {
        val viewPager: ViewPager2 = view.findViewById(R.id.pager)
        val classLoader = requireActivity().classLoader
        val fragFactory = requireActivity().supportFragmentManager.fragmentFactory

        viewPager.adapter =
            MyAdapter(
                this,
                fragFactory,
                classLoader
            )

        viewPager.clipToPadding = false
        viewPager.clipChildren = false
        viewPager.offscreenPageLimit = 2
        viewPager.setPadding(40, 0, 40, 0)

        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.title_special_blend)
                1 -> getString(R.string.title_match)
                else -> throw IllegalArgumentException("Unknown pager position: $position")
            }
        }.attach()
    }
}

private class MyAdapter(
    fragment: Fragment,
    private val fragFactory: FragmentFactory,
    private val classLoader: ClassLoader
) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        val fragName = when (position) {
            0 -> SpecialBlendFragment::class.java.canonicalName!!
            1 -> MatchPerctFragment::class.java.canonicalName!!
            else -> throw IllegalArgumentException("Unknown pager position: $position")
        }

        return fragFactory.instantiate(classLoader, fragName)
    }
}
