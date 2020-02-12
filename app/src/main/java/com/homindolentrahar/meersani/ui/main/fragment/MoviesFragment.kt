package com.homindolentrahar.meersani.ui.main.fragment


import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.homindolentrahar.meersani.R
import com.homindolentrahar.meersani.adapter.recyclerview.MoviesItemAdapter
import com.homindolentrahar.meersani.ui.main.fragment.viewmodel.MoviesViewModel
import com.homindolentrahar.meersani.util.Constants
import com.homindolentrahar.meersani.util.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_movies.*
import kotlinx.android.synthetic.main.loading_layout.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class MoviesFragment : DaggerFragment(), View.OnClickListener {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private lateinit var viewModel: MoviesViewModel
    private lateinit var nowPlayingAdapter: MoviesItemAdapter
    private lateinit var upcomingAdapter: MoviesItemAdapter
    private lateinit var popularAdapter: MoviesItemAdapter
    private lateinit var topRatedAdapter: MoviesItemAdapter
    private val disposable = CompositeDisposable()
    private val TAG = MoviesFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        ViewModel setup
        viewModel = ViewModelProviders.of(this, providerFactory).get(MoviesViewModel::class.java)
//        Setup Banner
        setupBanner()
//        Setup RecyclerView
        setupRecyclerView()
//        Observing data
        observingData()
//        See All button clicked
        buttonSeeAllClicked()
    }

    private fun setupBanner() {
        Glide.with(context!!).load(R.drawable.film)
            .apply(RequestOptions.bitmapTransform(Constants.getBannerOptions())).into(img_banner)
        btn_search.setOnClickListener {
            Constants.navigateToSearch(
                context!!,
                "Movies"
            )
        }
        btn_settings.setOnClickListener {
            Constants.navigateToSettings(context!!)
        }
    }

    private fun setupRecyclerView() {
        nowPlayingAdapter = MoviesItemAdapter(Constants.TYPE_PRIMARY_HOLDER) { item ->
            Constants.navigateToDetailItem(
                context!!,
                item.id,
                item.type
            )
        }
        upcomingAdapter = MoviesItemAdapter(Constants.TYPE_NORMAL_HOLDER) { item ->
            Constants.navigateToDetailItem(
                context!!,
                item.id,
                item.type
            )
        }
        popularAdapter = MoviesItemAdapter(Constants.TYPE_NORMAL_HOLDER) { item ->
            Constants.navigateToDetailItem(
                context!!,
                item.id,
                item.type
            )
        }
        topRatedAdapter = MoviesItemAdapter(Constants.TYPE_NORMAL_HOLDER) { item ->
            Constants.navigateToDetailItem(
                context!!,
                item.id,
                item.type
            )
        }

        rv_list_now_playing.adapter = nowPlayingAdapter
        rv_list_upcoming.adapter = upcomingAdapter
        rv_list_popular_movies.adapter = popularAdapter
        rv_list_top_rated_movies.adapter = topRatedAdapter
    }

    private fun observingData() {
        disposable.add(
            viewModel.getCachedMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { Constants.setProgressVisibility(loading_layout, View.VISIBLE) }
                .subscribe(
                    { list ->
                        nowPlayingAdapter.submitList(
                            list.filter { it.type == Constants.TYPE_MOVIES_NOW_PLAYING }.take(
                                10
                            )
                        )
                        upcomingAdapter.submitList(
                            list.filter { it.type == Constants.TYPE_MOVIES_UPCOMING }.take(
                                10
                            )
                        )
                        popularAdapter.submitList(
                            list.filter { it.type == Constants.TYPE_MOVIES_POPULAR }.take(
                                10
                            )
                        )
                        topRatedAdapter.submitList(
                            list.filter { it.type == Constants.TYPE_MOVIES_TOP_RATED }.take(
                                10
                            )
                        )

                        Log.d(TAG, "Received ${list.size} items from database")
                        Handler().postDelayed({
                            Constants.setProgressVisibility(loading_layout, View.GONE)
                        }, 800)
                    },
                    { error ->
                        Log.d(TAG, "Error Receiving data from database : ${error.message}")
                    }
                )
        )
    }

    private fun buttonSeeAllClicked() {
        btn_text_see_all_now_playing.setOnClickListener(this)
        btn_text_see_all_upcoming.setOnClickListener(this)
        btn_text_see_all_popular_movies.setOnClickListener(this)
        btn_text_see_all_top_rated_movies.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_text_see_all_now_playing -> Constants.navigateToCategories(
                context!!,
                Constants.TYPE_MOVIES_NOW_PLAYING
            )
            R.id.btn_text_see_all_upcoming -> Constants.navigateToCategories(
                context!!,
                Constants.TYPE_MOVIES_UPCOMING
            )
            R.id.btn_text_see_all_popular_movies -> Constants.navigateToCategories(
                context!!,
                Constants.TYPE_MOVIES_POPULAR
            )
            R.id.btn_text_see_all_top_rated_movies -> Constants.navigateToCategories(
                context!!,
                Constants.TYPE_MOVIES_TOP_RATED
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}
