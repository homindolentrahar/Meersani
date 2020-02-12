package com.homindolentrahar.meersani.data.paging

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.homindolentrahar.meersani.BuildConfig
import com.homindolentrahar.meersani.api.APIService
import com.homindolentrahar.meersani.model.GenresResult
import com.homindolentrahar.meersani.model.SeriesResult
import com.homindolentrahar.meersani.util.Constants
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SeriesDataSource(
    private val apiService: APIService,
    private val type: String,
    private val query: String,
    private val genresId: Int
) :
    PageKeyedDataSource<Int, SeriesResult>() {

    private val TAG = SeriesDataSource::class.java.simpleName
    private val disposable = CompositeDisposable()
    private val maxPage = 5

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, SeriesResult>
    ) {
        disposable.add(
            requestSeries(type, 1)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { list ->
                        callback.onResult(list, null, 2)
                        Log.d(TAG, "Page : 1,Items : ${list.size}")
                    },
                    { error ->
                        Log.d(TAG, "Error requesting data from Network : ${error.message}")
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, SeriesResult>) {
        if (params.key <= maxPage) {
            disposable.add(
                requestSeries(type, params.key)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        { list ->
                            callback.onResult(list, params.key + 1)
                            Log.d(TAG, "Page : ${params.key},Items : ${list.size}")
                        },
                        { error ->
                            Log.d(TAG, "Error requesting data from Network : ${error.message}")
                        }
                    )
            )
        } else {
            return
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, SeriesResult>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun requestSeries(type: String, page: Int): Flowable<List<SeriesResult>> {
        return apiService.getSeriesGenres(BuildConfig.API_KEY)
            .subscribeOn(Schedulers.io())
            .flatMap { response ->
                return@flatMap requestSeriesFromNetwork(type, response.genres, page)
            }
    }

    private fun requestSeriesFromNetwork(
        type: String,
        listGenres: List<GenresResult>,
        page: Int
    ): Flowable<List<SeriesResult>> {
        val service = when (type) {
            Constants.TYPE_SERIES_TODAY_AIRING -> apiService.getSeriesTodayAiring(
                BuildConfig.API_KEY,
                page
            )
            Constants.TYPE_SERIES_ON_AIR -> apiService.getSeriesOnAir(
                BuildConfig.API_KEY,
                page
            )
            Constants.TYPE_SERIES_POPULAR -> apiService.getSeriesPopular(
                BuildConfig.API_KEY,
                page
            )
            Constants.TYPE_SERIES_TOP_RATED -> apiService.getSeriesTopRated(
                BuildConfig.API_KEY,
                page
            )
            Constants.TYPE_SEARCH_SERIES -> apiService.searchSeries(
                BuildConfig.API_KEY,
                query,
                page
            )
            Constants.TYPE_SERIES_BY_GENRE -> apiService.getSeriesByGenres(
                BuildConfig.API_KEY,
                page,
                genresId
            )
            else -> null
        }
        return service
            ?.subscribeOn(Schedulers.io())
            ?.map { response ->
                response.results.forEach { item ->
                    item.genres = Constants.getGenres(listGenres, item.genreIds!!)
                }
                return@map response.results
            }!!
    }
}