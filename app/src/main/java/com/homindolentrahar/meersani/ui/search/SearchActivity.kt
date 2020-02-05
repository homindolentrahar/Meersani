package com.homindolentrahar.meersani.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.homindolentrahar.meersani.R
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : DaggerAppCompatActivity() {

    companion object {
        const val EXTRA_TYPE = "extra_type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
//        Getting item type
        val type = intent.getStringExtra(EXTRA_TYPE) as String
//        Setup FloatingSearchView
        setupFloatingSearch(type)
    }

    private fun setupFloatingSearch(type: String) {
        val searchHint = if (type.contains(
                "Movies",
                true
            )
        ) getString(R.string.movies) else getString(R.string.series)
        search_bar.setSearchHint(searchHint)
        search_bar.setOnHomeActionClickListener { finish() }
    }
}
