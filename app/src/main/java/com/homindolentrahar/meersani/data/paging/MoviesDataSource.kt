package com.homindolentrahar.meersani.data.paging

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.homindolentrahar.meersani.BuildConfig
import com.homindolentrahar.meersani.api.APIService
import com.homindolentrahar.meersani.model.GenresResult
import com.homindolentrahar.meersani.model.MoviesResult
import com.homindolentrahar.meersani.util.Constants
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MoviesDataSource(
    private val apiService: APIService,
    private val type: String,
    private val query: String,
    private val genresId: Int
) :
    PageKeyedDataSource<Int, MoviesResult>() {

    private val TAG = MoviesDataSource::class.java.simpleName
    private val disposable = CompositeDisposable()
    private val maxPage = 5

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, MoviesResult>
    ) {
        disposable.add(
            requestMovies(type, 1)
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

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MoviesResult>) {
        if (params.key <= maxPage) {
            disposable.add(
                requestMovies(type, params.key)
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

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MoviesResult>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun requestMovies(type: String, page: Int): Flowable<List<MoviesResult>> {
        return apiService.getMoviesGenres(BuildConfig.API_KEY)
            .subscribeOn(Schedulers.io())
            .flatMap { response ->
                return@flatMap requestMoviesFromNetwork(type, response.genres, page)
            }
    }

    private fun requestMoviesFromNetwork(
        type: String,
        listGenres: List<GenresResult>,
        page: Int
    ): Flowable<List<MoviesResult>> {
        val service = when (type) {
            Constants.TYPE_MOVIES_NOW_PLAYING -> apiService.getMoviesNowPlaying(
                BuildConfig.API_KEY,
                page
            )
            Constants.TYPE_MOVIES_UPCOMING -> apiService.getMoviesUpcoming(
                BuildConfig.API_KEY,
                page
            )
            Constants.TYPE_MOVIES_POPULAR -> apiService.getMoviesPopular(
                BuildConfig.API_KEY,
                page
            )
            Constants.TYPE_MOVIES_TOP_RATED -> apiService.getMoviesTopRated(
                BuildConfig.API_KEY,
                page
            )
            Constants.TYPE_SEARCH_MOVIES -> apiService.searchMovies(
                BuildConfig.API_KEY,
                query,
                page
            )
            Constants.TYPE_MOVIES_BY_GENRE -> apiService.getMoviesByGenres(
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