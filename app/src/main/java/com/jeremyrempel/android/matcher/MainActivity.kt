package com.jeremyrempel.android.matcher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jeremyrempel.android.matcher.features.search.view.SearchMainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApplication).apply {
            supportFragmentManager.fragmentFactory = dagger.fragFactory()
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val frag = supportFragmentManager.fragmentFactory.instantiate(
                classLoader, SearchMainFragment::class.java.canonicalName!!
            )

            println("testing")

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container_view, frag)
                .commitNow()
        }
    }
}
