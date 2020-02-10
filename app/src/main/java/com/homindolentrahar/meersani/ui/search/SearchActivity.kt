package com.homindolentrahar.meersani.ui.search

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.homindolentrahar.meersani.BaseActivity
import com.homindolentrahar.meersani.R
import com.homindolentrahar.meersani.adapter.recyclerview.PagedMoviesItemAdapter
import com.homindolentrahar.meersani.adapter.recyclerview.PagedSeriesItemAdapter
import com.homindolentrahar.meersani.util.Constants
import com.homindolentrahar.meersani.util.ViewModelProviderFactory
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchActivity : BaseActivity() {

    companion object {
        const val EXTRA_TYPE = "extra_type"
    }

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private lateinit var viewModel: SearchViewModel
    private lateinit var moviesAdapter: PagedMoviesItemAdapter
    private lateinit var seriesAdapter: PagedSeriesItemAdapter

    private val TAG = SearchActivity::class.java.simpleName
    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
//        Getting item type
        val type = intent.getStringExtra(EXTRA_TYPE) as String
//        ViewModel setup
        viewModel = ViewModelProviders.of(this, providerFactory).get(SearchViewModel::class.java)
//        Setup FloatingSearchView
        setupFloatingSearch(type)
//        RecyclerView setup
        setupRecyclerView(type)
//        Set Search item
        setSearch(type)
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

    private fun setupRecyclerView(type: String) {
        moviesAdapter = PagedMoviesItemAdapter { item ->
            Constants.navigateToDetailItem(this, item.id, type)
        }
        seriesAdapter = PagedSeriesItemAdapter { item ->
            Constants.navigateToDetailItem(this, item.id, type)
        }
        rv_list_search.adapter = if (type.contains("Movies", true)) moviesAdapter else seriesAdapter
    }

    private fun setSearch(type: String) {
        val observableSearch = Observable.create<String> { emitter ->
            search_bar.setOnQueryChangeListener { oldQuery, newQuery ->
                emitter.onNext(newQuery)
            }
        }
            .debounce(800, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())

        disposable.add(
            observableSearch
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { query ->
                        progress_bar.visibility = View.VISIBLE
                        if (type.contains("Movies", true)) searchMovies(query)
                        else searchSeries(query)
                        Log.d(TAG, "Search : $query")
                        Handler().postDelayed({
                            progress_bar.visibility = View.GONE
                        }, 800)
                    },
                    { error ->
                        Log.d(TAG, "Error searching : ${error.message}")
                    }
                )
        )
    }

    private fun searchMovies(query: String) {
        viewModel.searchMovies(query).observe(this, Observer {
            if (it != null) {
                moviesAdapter.submitList(it)
                Log.d(TAG, "Searched items of $query : ${it.size}")
            }
        })
    }

    private fun searchSeries(query: String) {
        viewModel.searchSeries(query).observe(this, Observer {
            if (it != null) {
                seriesAdapter.submitList(it)
                Log.d(TAG, "Searched items of $query : ${it.size}")
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}
