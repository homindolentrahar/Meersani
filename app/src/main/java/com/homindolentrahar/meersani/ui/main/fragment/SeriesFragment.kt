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
import com.homindolentrahar.meersani.adapter.recyclerview.SeriesItemAdapter
import com.homindolentrahar.meersani.ui.main.fragment.viewmodel.SeriesViewModel
import com.homindolentrahar.meersani.util.Constants
import com.homindolentrahar.meersani.util.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_series.*
import kotlinx.android.synthetic.main.loading_layout.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class SeriesFragment : DaggerFragment(), View.OnClickListener {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private lateinit var viewModel: SeriesViewModel
    private lateinit var todayAiringAdapter: SeriesItemAdapter
    private lateinit var onAirAdapter: SeriesItemAdapter
    private lateinit var popularAdapter: SeriesItemAdapter
    private lateinit var topRatedAdapter: SeriesItemAdapter
    private val disposable = CompositeDisposable()
    private val TAG = SeriesFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_series, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        ViewModel setup
        viewModel = ViewModelProviders.of(this, providerFactory).get(SeriesViewModel::class.java)
//        Banner setup
        setupBanner()
//        RecyclerView setup
        setupRecyclerView()
//        Observing data
        observingData()
//        See All button clicked
        buttonSeeAllClicked()
    }

    private fun setupBanner() {
        Glide.with(context!!).load(R.drawable.tv)
            .apply(RequestOptions.bitmapTransform(Constants.getBannerOptions())).into(img_banner)
        btn_search.setOnClickListener {
            Constants.navigateToSearch(
                context!!,
                "Series"
            )
        }
        btn_settings.setOnClickListener { Constants.navigateToSettings(context!!) }
    }

    private fun setupRecyclerView() {
        todayAiringAdapter = SeriesItemAdapter(Constants.TYPE_PRIMARY_HOLDER) { item ->
            Constants.navigateToDetailItem(
                context!!,
                item.id,
                item.type
            )
        }
        onAirAdapter = SeriesItemAdapter(Constants.TYPE_NORMAL_HOLDER) { item ->
            Constants.navigateToDetailItem(
                context!!,
                item.id,
                item.type
            )
        }
        popularAdapter = SeriesItemAdapter(Constants.TYPE_NORMAL_HOLDER) { item ->
            Constants.navigateToDetailItem(
                context!!,
                item.id,
                item.type
            )
        }
        topRatedAdapter = SeriesItemAdapter(Constants.TYPE_NORMAL_HOLDER) { item ->
            Constants.navigateToDetailItem(
                context!!,
                item.id,
                item.type
            )
        }

        rv_list_today_airing.adapter = todayAiringAdapter
        rv_list_on_air.adapter = onAirAdapter
        rv_list_popular_series.adapter = popularAdapter
        rv_list_top_rated_series.adapter = topRatedAdapter
    }

    private fun observingData() {
        disposable.add(
            viewModel.getCachedSeries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { Constants.setProgressVisibility(loading_layout, View.VISIBLE) }
                .subscribe(
                    { list ->
                        todayAiringAdapter.submitList(
                            list.filter { it.type == Constants.TYPE_SERIES_TODAY_AIRING }.take(
                                10
                            )
                        )
                        onAirAdapter.submitList(
                            list.filter { it.type == Constants.TYPE_SERIES_ON_AIR }.take(
                                10
                            )
                        )
                        popularAdapter.submitList(
                            list.filter { it.type == Constants.TYPE_SERIES_POPULAR }.take(
                                10
                            )
                        )
                        topRatedAdapter.submitList(
                            list.filter { it.type == Constants.TYPE_SERIES_TOP_RATED }.take(
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
        btn_text_see_all_today_airing.setOnClickListener(this)
        btn_text_see_all_on_air.setOnClickListener(this)
        btn_text_see_all_popular_series.setOnClickListener(this)
        btn_text_see_all_top_rated_series.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_text_see_all_today_airing -> Constants.navigateToCategories(
                context!!,
                Constants.TYPE_SERIES_TODAY_AIRING
            )
            R.id.btn_text_see_all_on_air -> Constants.navigateToCategories(
                context!!,
                Constants.TYPE_SERIES_ON_AIR
            )
            R.id.btn_text_see_all_popular_series -> Constants.navigateToCategories(
                context!!,
                Constants.TYPE_SERIES_POPULAR
            )
            R.id.btn_text_see_all_top_rated_series -> Constants.navigateToCategories(
                context!!,
                Constants.TYPE_SERIES_TOP_RATED
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

}
