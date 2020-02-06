package com.homindolentrahar.meersani.ui.main.fragment.favorites


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders

import com.homindolentrahar.meersani.R
import com.homindolentrahar.meersani.adapter.recyclerview.FavoritesItemAdapter
import com.homindolentrahar.meersani.adapter.recyclerview.MoviesItemAdapter
import com.homindolentrahar.meersani.ui.main.fragment.favorites.viewmodel.FavoritesMoviesViewModel
import com.homindolentrahar.meersani.util.Constants
import com.homindolentrahar.meersani.util.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_favorites_movies.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class FavoritesMoviesFragment : DaggerFragment() {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private lateinit var viewModel: FavoritesMoviesViewModel
    private lateinit var adapter: FavoritesItemAdapter
    private val TAG = FavoritesMoviesFragment::class.java.simpleName
    private val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        ViewModel setup
        viewModel =
            ViewModelProviders.of(this, providerFactory).get(FavoritesMoviesViewModel::class.java)
//        RecyclerView setup
        setupRecyclerView()
//        Observing data
        observingData()
    }

    private fun setupRecyclerView() {
        adapter = FavoritesItemAdapter { item ->
            Constants.navigateToDetailItem(context!!, item.itemId, item.type)
        }
        rv_list_favorites.adapter = adapter
    }

    private fun observingData() {
        disposable.add(
            viewModel.getFavoritesMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { list ->
                        adapter.submitList(list)
                        Log.d(TAG, "Getting ${list.size} data")
                    },
                    { error ->
                        Log.d(TAG, "Error getting data : ${error.message}")
                    }
                )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}
