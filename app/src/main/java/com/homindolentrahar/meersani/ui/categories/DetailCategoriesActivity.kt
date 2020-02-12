package com.homindolentrahar.meersani.ui.categories

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.homindolentrahar.meersani.BaseActivity
import com.homindolentrahar.meersani.R
import com.homindolentrahar.meersani.adapter.recyclerview.PagedMoviesItemAdapter
import com.homindolentrahar.meersani.adapter.recyclerview.PagedSeriesItemAdapter
import com.homindolentrahar.meersani.util.Constants
import com.homindolentrahar.meersani.util.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_detail_categories.*
import javax.inject.Inject

class DetailCategoriesActivity : BaseActivity() {

    companion object {
        const val EXTRA_TYPE = "extra_type"
    }

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private lateinit var viewModel: DetailCategoriesViewModel
    private lateinit var moviesAdapter: PagedMoviesItemAdapter
    private lateinit var seriesAdapter: PagedSeriesItemAdapter
    private val TAG = DetailCategoriesActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_categories)
//        Getting item type
        val type = intent.getStringExtra(EXTRA_TYPE) as String
//        ViewModel setup
        viewModel =
            ViewModelProviders.of(this, providerFactory).get(DetailCategoriesViewModel::class.java)
//        Banner setup
        setupBanner(type)
//        RecyclerView setup
        setupRecyclerView(type)
//        Observing data
        observingData(type)
    }

    private fun setupBanner(type: String) {
        val bannerTitle = when (type) {
            Constants.TYPE_MOVIES_NOW_PLAYING -> getString(R.string.now_playing)
            Constants.TYPE_MOVIES_UPCOMING -> getString(R.string.upcoming)
            Constants.TYPE_MOVIES_POPULAR -> getString(R.string.popular)
            Constants.TYPE_MOVIES_TOP_RATED -> getString(R.string.top_rated)
            Constants.TYPE_SERIES_TODAY_AIRING -> getString(R.string.today_airing)
            Constants.TYPE_SERIES_ON_AIR -> getString(R.string.on_air)
            Constants.TYPE_SERIES_POPULAR -> getString(R.string.popular)
            Constants.TYPE_SERIES_TOP_RATED -> getString(R.string.top_rated)
            else -> "No Title"
        }
        val bannerImage = if (type.contains("Movies", true)) R.drawable.film else R.drawable.tv
        Glide.with(this).load(bannerImage)
            .apply(RequestOptions.bitmapTransform(Constants.getBannerOptions())).into(img_banner)
        text_banner_title.text = bannerTitle
        btn_back.setOnClickListener { finish() }
        btn_search.setOnClickListener { Constants.navigateToSearch(this, type) }
        btn_settings.setOnClickListener { Constants.navigateToSettings(this) }
    }

    private fun setupRecyclerView(type: String) {
        moviesAdapter = PagedMoviesItemAdapter { item ->
            Constants.navigateToDetailItem(this, item.id, type)
        }
        seriesAdapter = PagedSeriesItemAdapter { item ->
            Constants.navigateToDetailItem(this, item.id, type)
        }
        rv_list_detail.adapter = if (type.contains("Movies", true)) moviesAdapter else seriesAdapter
    }

    private fun observingData(type: String) {
        if (type.contains("Movies", true)) {
            viewModel.getPagedMovies(type).observe(this, Observer {
                if (it != null) {
                    moviesAdapter.submitList(it)
                }
            })
        } else {
            viewModel.getPagedSeries(type).observe(this, Observer {
                if (it != null) {
                    seriesAdapter.submitList(it)
                }
            })
        }
    }
}
