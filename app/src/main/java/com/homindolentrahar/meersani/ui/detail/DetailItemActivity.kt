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
import com.google.android.material.snackbar.Snackbar
import com.homindolentrahar.meersani.BaseActivity
import com.homindolentrahar.meersani.BuildConfig
import com.homindolentrahar.meersani.R
import com.homindolentrahar.meersani.adapter.recyclerview.CastItemAdapter
import com.homindolentrahar.meersani.adapter.recyclerview.MoviesItemAdapter
import com.homindolentrahar.meersani.adapter.recyclerview.SeriesItemAdapter
import com.homindolentrahar.meersani.model.*
import com.homindolentrahar.meersani.util.Constants
import com.homindolentrahar.meersani.util.ViewModelProviderFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail_item.*
import kotlinx.android.synthetic.main.loading_layout.*
import javax.inject.Inject

class DetailItemActivity : BaseActivity() {

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
    private var isLoved = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_item)
//        Get item
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val type = intent.getStringExtra(EXTRA_TYPE) as String
//        ViewModel setup
        viewModel =
            ViewModelProviders.of(this, providerFactory).get(DetailItemViewModel::class.java)
//        Check existed item on favorties
        checkExistedItemOnFavorites(id)
//        Banner setup
        setupBanner()
//        RecyclerView setup
        setupRecyclerView(type)
//        Setup item
        setItem(id, type)
    }

    private fun checkExistedItemOnFavorites(id: Int) {
        disposable.add(
            viewModel.checkExistedItem(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { list ->
                        isLoved = list.isNotEmpty()
                        loveState(isLoved)
                        Log.d(TAG, "Is Favorites : ${list.isNotEmpty()}")
                    },
                    { error ->
                        Log.d(TAG, "Error checking : ${error.message}")
                    }
                )
        )
    }

    private fun setupBanner() {
        btn_back.setOnClickListener { finish() }
        btn_settings.setOnClickListener { Constants.navigateToSettings(this) }
    }

    private fun setupRecyclerView(type: String) {
        moviesAdapter = MoviesItemAdapter(Constants.TYPE_NORMAL_HOLDER) { item ->
            Constants.navigateToDetailItem(this, item.id, type)
            finish()
        }
        seriesAdapter = SeriesItemAdapter(Constants.TYPE_NORMAL_HOLDER) { item ->
            Constants.navigateToDetailItem(this, item.id, type)
            finish()
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
                            Log.d(TAG, "Getting detail Movies")
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
                            Log.d(TAG, "Getting detail Series")
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
            item.id,
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
            item.id,
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
        id: Int,
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
            chip.id = genre.id
            chip.text = genre.name
            chip.setOnClickListener { Constants.navigateToGenres(this, genre.id, genre.name, type) }
            chip_genre_group.addView(chip)
        }
        tvReadMoreListener()
//        Setup Favorites Operations
        val favType = if (type.contains(
                "Movies",
                true
            )
        ) Constants.FAVORITES_MOVIES else Constants.FAVORITES_SERIES
        setupFavoritesOperations(
            id,
            title,
            release,
            Constants.getStringGenres(listGenre),
            rating.toDouble(),
            posterPath,
            favType
        )
    }

    private fun tvReadMoreListener() {
        tv_overview.setOnClickListener {
            if (!expanded) {
                expanded = true
                val animator = ObjectAnimator.ofInt(tv_overview, "maxLines", 24)
                animator.setDuration(100).start()
            } else {
                expanded = false
                val animator = ObjectAnimator.ofInt(tv_overview, "maxLines", 3)
                animator.setDuration(100).start()
            }
        }
    }

    private fun setupFavoritesOperations(
        id: Int,
        title: String,
        release: String,
        genres: String,
        rating: Double,
        posterPath: String,
        type: String
    ) {
        btn_favorite.setOnClickListener {

            if (isLoved) {
                disposable.add(
                    viewModel.delete(id)
                        .subscribeOn(Schedulers.io())
                        .subscribe {
                            showSnackbar("Removed from your Favorites")
                        }
                )
            } else {
                val favorites = Favorites(id, title, release, genres, rating, posterPath, type)
                disposable.add(
                    viewModel.insert(favorites)
                        .subscribeOn(Schedulers.io())
                        .subscribe {
                            showSnackbar("Added to your Favorites")
                        }
                )
            }

            isLoved = !isLoved
            loveState(isLoved)
        }
    }

    private fun loveState(isLoved: Boolean) {
        if (isLoved) btn_favorite.setImageResource(R.drawable.ic_heart_filled)
        else btn_favorite.setImageResource(R.drawable.ic_heart)
    }

    private fun showSnackbar(msg: String) {
        Snackbar.make(all_wrapper, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}
