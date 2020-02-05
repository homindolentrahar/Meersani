package com.homindolentrahar.meersani.ui.detail

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.chip.Chip
import com.homindolentrahar.meersani.BuildConfig
import com.homindolentrahar.meersani.R
import com.homindolentrahar.meersani.adapter.recyclerview.CastItemAdapter
import com.homindolentrahar.meersani.adapter.recyclerview.MoviesItemAdapter
import com.homindolentrahar.meersani.adapter.recyclerview.SeriesItemAdapter
import com.homindolentrahar.meersani.model.*
import com.homindolentrahar.meersani.util.Constants
import com.homindolentrahar.meersani.util.ViewModelProviderFactory
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail_item.*
import kotlinx.android.synthetic.main.loading_layout.*
import javax.inject.Inject

class DetailItemActivity : DaggerAppCompatActivity() {

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_TYPE = "extra_type"
    }

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private lateinit var viewModel: DetailItemViewModel
    private lateinit var moviesAdapter: MoviesItemAdapter
    private lateinit var seriesAdapter: SeriesItemAdapter
    private lateinit var castAdapter: CastItemAdapter
    private val TAG = DetailItemActivity::class.java.simpleName
    private val disposable = CompositeDisposable()
    private var expanded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_item)
//        Get item
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val type = intent.getStringExtra(EXTRA_TYPE) as String
//        ViewModel setup
        viewModel =
            ViewModelProviders.of(this, providerFactory).get(DetailItemViewModel::class.java)
//        Banner setup
        setupBanner()
//        RecyclerView setup
        setupRecyclerView(type)
//        Setup item
        setItem(id, type)
    }

    private fun setupBanner() {
        btn_back.setOnClickListener { finish() }
    }

    private fun setupRecyclerView(type: String) {
        moviesAdapter = MoviesItemAdapter(Constants.TYPE_NORMAL_HOLDER) { item ->
            Constants.navigateToDetailItem(this, item.id, type)
        }
        seriesAdapter = SeriesItemAdapter(Constants.TYPE_NORMAL_HOLDER) { item ->
            Constants.navigateToDetailItem(this, item.id, type)
        }
        castAdapter = CastItemAdapter()
        rv_list_recommendations.adapter =
            if (type.contains("Movies", true)) moviesAdapter else seriesAdapter
        rv_list_cast.adapter = castAdapter
    }

    private fun setItem(id: Int, type: String) {
        if (type.contains("Movies", true)) {
            disposable.add(
                viewModel.getDetailMovies(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { Constants.setProgressVisibility(loading_layout, View.VISIBLE) }
                    .subscribe(
                        { detailMovies ->
                            setMoviesItem(detailMovies.moviesDetail, type)
                            setMoviesRecommendations(detailMovies.moviesRecommendations)
                            setCast(detailMovies.cast.cast)
                            Handler().postDelayed({
                                Constants.setProgressVisibility(loading_layout, View.GONE)
                            }, 800)
                            Log.d(TAG, "Success mungkin ?")
                        },
                        { error ->
                            Log.d(TAG, "Error setup detail : ${error.message}")
                        }
                    )
            )
        } else {
            disposable.add(
                viewModel.getDetailSeries(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { Constants.setProgressVisibility(loading_layout, View.VISIBLE) }
                    .subscribe(
                        { detailSeries ->
                            setSeriesItem(detailSeries.seriesDetail, type)
                            setSeriesRecommendations(detailSeries.seriesRecommendations)
                            setCast(detailSeries.cast.cast)
                            Handler().postDelayed({
                                Constants.setProgressVisibility(loading_layout, View.GONE)
                            }, 800)
                            Log.d(TAG, "Success mungkin ?")
                        },
                        { error ->
                            Log.d(TAG, "Error setup detail : ${error.message}")
                        }
                    )
            )
        }
    }

    private fun setMoviesItem(item: MoviesDetail, type: String) {
        populateToView(
            item.title,
            Constants.getFormattedReleaseDate(item.releaseDate),
            item.genres,
            BuildConfig.IMAGE_BASE_URL + item.backdropPath,
            BuildConfig.IMAGE_BASE_URL + item.posterPath,
            item.rating.toString(),
            getString(R.string.runtime_format, item.runtime.toString()),
            Constants.getFormattedCurrency(item.budget),
            Constants.getFormattedCurrency(item.revenue),
            getString(R.string.detail_text_format, item.overview),
            Constants.getProductionHouse(item.productionCompanies),
            type
        )
    }

    private fun setSeriesItem(item: SeriesDetail, type: String) {
        populateToView(
            item.name,
            Constants.getFormattedReleaseDate(item.firstAirDate),
            item.genres,
            BuildConfig.IMAGE_BASE_URL + item.backdropPath,
            BuildConfig.IMAGE_BASE_URL + item.posterPath,
            item.rating.toString(),
            getString(R.string.runtime_format, Constants.getEpisodesRuntime(item.runtime)),
            item.episodes.toString(),
            item.seasons.toString(),
            getString(R.string.detail_text_format, item.overview),
            Constants.getProductionHouse(item.productionCompanies),
            type
        )
    }

    private fun setMoviesRecommendations(list: List<MoviesResult>) {
        moviesAdapter.submitList(list)
    }

    private fun setSeriesRecommendations(list: List<SeriesResult>) {
        seriesAdapter.submitList(list)
    }

    private fun setCast(list: List<CastResult>) {
        castAdapter.submitList(list)
    }

    private fun populateToView(
        title: String,
        release: String,
        listGenre: List<GenresResult>,
        backdropPath: String,
        posterPath: String,
        rating: String,
        runtime: String,
        budget_or_episodes: String,
        revenue_or_seasons: String,
        overview: String,
        production: String,
        type: String
    ) {
        if (type.contains("Movies", true)) {
            text_budget_or_episodes.text = getString(R.string.budget)
            text_revenue_seasons.text = getString(R.string.revenue)
        } else {
            text_budget_or_episodes.text = getString(R.string.episodes)
            text_revenue_seasons.text = getString(R.string.seasons)
        }
        tv_title.text = title
        tv_release.text = release
        tv_rating.text = rating
        tv_runtime.text = runtime
        tv_budget_or_episodes.text = budget_or_episodes
        tv_revenue_seasons.text = revenue_or_seasons
        tv_overview.text = overview
        tv_production.text = production
        Glide.with(this).load(backdropPath)
            .apply(RequestOptions.bitmapTransform(Constants.getBannerOptions()))
            .into(img_backdrop)
        Glide.with(this).load(posterPath).into(img_poster)
        for (genre in listGenre) {
            val chip = layoutInflater.inflate(R.layout.chip_single_item, null, false) as Chip
            chip.text = genre.name
            chip_genre_group.addView(chip)
        }
        tvReadMoreListener()
    }

    private fun tvReadMoreListener() {
        if (tv_overview.lineCount >= 3) {
            btn_text_read_more.visibility = View.VISIBLE
        }
        btn_text_read_more.setOnClickListener {
            if (!expanded) {
                expanded = true
                val animator = ObjectAnimator.ofInt(tv_overview, "maxLines", 24)
                animator.setDuration(100).start()
                btn_text_read_more.text = getString(R.string.hide)
            } else {
                expanded = false
                val animator = ObjectAnimator.ofInt(tv_overview, "maxLines", 3)
                animator.setDuration(100).start()
                btn_text_read_more.text = getString(R.string.read_more)
            }
        }
    }
}
