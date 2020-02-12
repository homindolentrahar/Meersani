package com.homindolentrahar.meersani.ui.genre

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.homindolentrahar.meersani.BaseActivity
import com.homindolentrahar.meersani.R
import com.homindolentrahar.meersani.adapter.recyclerview.PagedMoviesItemAdapter
import com.homindolentrahar.meersani.adapter.recyclerview.PagedSeriesItemAdapter
import com.homindolentrahar.meersani.util.Constants
import com.homindolentrahar.meersani.util.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_genres.*
import javax.inject.Inject

class GenresActivity : BaseActivity() {

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_TYPE = "extra_type"
    }

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private lateinit var viewModel: GenresViewModel
    private lateinit var moviesAdapter: PagedMoviesItemAdapter
    private lateinit var seriesAdapter: PagedSeriesItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genres)
//        Getting Genres ID,Name,and Type
        val genresId = intent.getIntExtra(EXTRA_ID, 0)
        val name = intent.getStringExtra(EXTRA_NAME) as String
        val type = intent.getStringExtra(EXTRA_TYPE) as String
//        ViewModel setup
        viewModel = ViewModelProviders.of(this, providerFactory).get(GenresViewModel::class.java)
//        Back button
        btn_back.setOnClickListener { finish() }
//        Setting Genre title
        tv_genre.text = name
//        RecyclerView setup
        setupRecyclerView(type)
//        Observing Data
        observingData(genresId, type)
    }

    private fun setupRecyclerView(type: String) {
        moviesAdapter = PagedMoviesItemAdapter { item ->
            Constants.navigateToDetailItem(this, item.id, type)
        }
        seriesAdapter = PagedSeriesItemAdapter { item ->
            Constants.navigateToDetailItem(this, item.id, type)
        }
        rv_list_genre.adapter = if (type.contains("Movies", true)) moviesAdapter else seriesAdapter
    }

    private fun observingData(genresId: Int, type: String) {
        if (type.contains("Movies", true)) {
            viewModel.getMoviesByGenres(genresId).observe(this, Observer {
                if (it != null) {
                    moviesAdapter.submitList(it)
                }
            })
        } else {
            viewModel.getSeriesByGenres(genresId).observe(this, Observer {
                if (it != null) {
                    seriesAdapter.submitList(it)
                }
            })
        }
    }
}
