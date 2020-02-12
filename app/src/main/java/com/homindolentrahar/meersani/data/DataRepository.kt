package com.homindolentrahar.meersani.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.homindolentrahar.meersani.BuildConfig
import com.homindolentrahar.meersani.api.APIService
import com.homindolentrahar.meersani.data.paging.MoviesDataSourceFactory
import com.homindolentrahar.meersani.data.paging.SeriesDataSourceFactory
import com.homindolentrahar.meersani.db.FavoritesDao
import com.homindolentrahar.meersani.model.*
import com.homindolentrahar.meersani.util.Constants
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val apiService: APIService,
    private val localCache: LocalCache,
    private val favoritesDao: FavoritesDao
) {

    private val TAG = DataRepository::class.java.simpleName
    private val disposable = CompositeDisposable()
    private val pageSize = 10

//    Get Items by Genres
//    Get Movies by Genres

    fun getMoviesByGenres(genresId: Int): LiveData<PagedList<MoviesResult>> {
        val dataSourceFactory = MoviesDataSourceFactory(
            apiService,
            Constants.TYPE_MOVIES_BY_GENRE,
            Constants.NO_QUERY,
            genresId
        )
        return LivePagedListBuilder(
            dataSourceFactory,
            Constants.getPagedListConfig(pageSize)
        ).build()
    }

//    Get Series by Genres

    fun getSeriesByGenres(genresId: Int): LiveData<PagedList<SeriesResult>> {
        val dataSourceFactory = SeriesDataSourceFactory(
            apiService,
            Constants.TYPE_SERIES_BY_GENRE,
            Constants.NO_QUERY,
            genresId
        )
        return LivePagedListBuilder(
            dataSourceFactory,
            Constants.getPagedListConfig(pageSize)
        ).build()
    }

//    Favorites database Operations

    fun checkExistedItem(itemId: Int): Flowable<List<Favorites>> {
        return favoritesDao.checkExistedItem(itemId)
    }

    fun getFavoritesByType(type: String): Flowable<List<Favorites>> {
        return favoritesDao.getFavoritesByType(type)
    }

    fun insert(item: Favorites): Completable {
        return favoritesDao.insert(item)
    }

    fun delete(itemId: Int): Completable {
        return favoritesDao.delete(itemId)
    }

//    Detail

    fun getDetailMovies(id: Int): Flowable<DetailMovies> {
        return Flowable.zip(
            apiService.getMoviesDetail(id, BuildConfig.API_KEY).subscribeOn(Schedulers.io()),
            apiService.getMoviesCast(id, BuildConfig.API_KEY).subscribeOn(Schedulers.io()),
            apiService.getMoviesRecommendations(
                id,
                BuildConfig.API_KEY
            ).subscribeOn(Schedulers.io()),
            Function3 { moviesDetail, cast, moviesRecommendations ->
                DetailMovies(moviesDetail, cast, moviesRecommendations.results)
            }
        )
    }

    fun getDetailSeries(id: Int): Flowable<DetailSeries> {
        return Flowable.zip(
            apiService.getSeriesDetail(id, BuildConfig.API_KEY).subscribeOn(Schedulers.io()),
            apiService.getSeriesCast(id, BuildConfig.API_KEY).subscribeOn(Schedulers.io()),
            apiService.getSeriesRecommendations(
                id,
                BuildConfig.API_KEY
            ).subscribeOn(Schedulers.io()),
            Function3 { seriesDetail, cast, seriesRecommendations ->
                DetailSeries(seriesDetail, cast, seriesRecommendations.results)
            }
        )
    }

//    Paging

    fun getPagedMovies(type: String): LiveData<PagedList<MoviesResult>> {
        val dataSourceFactory = MoviesDataSourceFactory(apiService, type, Constants.NO_QUERY, 0)
        return LivePagedListBuilder(
            dataSourceFactory,
            Constants.getPagedListConfig(pageSize)
        ).build()
    }

    fun getPagedSeries(type: String): LiveData<PagedList<SeriesResult>> {
        val dataSourceFactory = SeriesDataSourceFactory(apiService, type, Constants.NO_QUERY, 0)
        return LivePagedListBuilder(
            dataSourceFactory,
            Constants.getPagedListConfig(pageSize)
        ).build()
    }

    fun searchMovies(query: String): LiveData<PagedList<MoviesResult>> {
        val dataSourceFactory =
            MoviesDataSourceFactory(apiService, Constants.TYPE_SEARCH_MOVIES, query, 0)
        return LivePagedListBuilder(
            dataSourceFactory,
            Constants.getPagedListConfig(pageSize)
        ).build()
    }

    fun searchSeries(query: String): LiveData<PagedList<SeriesResult>> {
        val dataSourceFactory =
            SeriesDataSourceFactory(apiService, Constants.TYPE_SEARCH_SERIES, query, 0)
        return LivePagedListBuilder(
            dataSourceFactory,
            Constants.getPagedListConfig(pageSize)
        ).build()
    }

//    Caching

    fun getCachedMovies(): Flowable<List<MoviesResult>> {
        return localCache.getAllCachedMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { list ->
                if (list.isEmpty()) cachingMoviesToDatabase()
            }
    }

    fun getCachedSeries(): Flowable<List<SeriesResult>> {
        return localCache.getAllCachedSeries()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { list ->
                if (list.isEmpty()) cachingSeriesToDatabase()
            }
    }

    fun clearDisposable() {
        disposable.clear()
    }

//    Caching Main page to Database

    private fun cachingMoviesToDatabase() {
        disposable.add(
            combineRequestMovies()
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { list ->
                        localCache.insertAllMovies(list)
                        Log.d(TAG, "Inserting ${list.size} movies items to Database")
                    },
                    { error ->
                        Log.d(TAG, "Error caching movies : ${error.message}")
                    }
                )
        )
    }

    private fun cachingSeriesToDatabase() {
        disposable.add(
            combineRequestSeries()
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { list ->
                        localCache.insertAllSeries(list)
                        Log.d(TAG, "Inserting ${list.size} series items to Database")
                    },
                    { error ->
                        Log.d(TAG, "Error caching series : ${error.message}")
                    }
                )
        )
    }

    private fun combineRequestMovies(): Flowable<List<MoviesResult>> {
        return Flowable.merge(
            requestMovies(Constants.TYPE_MOVIES_NOW_PLAYING).subscribeOn(Schedulers.io()),
            requestMovies(Constants.TYPE_MOVIES_UPCOMING).subscribeOn(Schedulers.io()),
            requestMovies(Constants.TYPE_MOVIES_POPULAR).subscribeOn(Schedulers.io()),
            requestMovies(Constants.TYPE_MOVIES_TOP_RATED).subscribeOn(Schedulers.io())
        )
    }

    private fun combineRequestSeries(): Flowable<List<SeriesResult>> {
        return Flowable.merge(
            requestSeries(Constants.TYPE_SERIES_TODAY_AIRING).subscribeOn(Schedulers.io()),
            requestSeries(Constants.TYPE_SERIES_ON_AIR).subscribeOn(Schedulers.io()),
            requestSeries(Constants.TYPE_SERIES_POPULAR).subscribeOn(Schedulers.io()),
            requestSeries(Constants.TYPE_SERIES_TOP_RATED).subscribeOn(Schedulers.io())
        )
    }

    private fun requestMovies(type: String): Flowable<List<MoviesResult>> {
        return apiService.getMoviesGenres(BuildConfig.API_KEY)
            .subscribeOn(Schedulers.io())
            .flatMap { response ->
                return@flatMap requestMoviesFromNetwork(type, response.genres)
            }
    }

    private fun requestSeries(type: String): Flowable<List<SeriesResult>> {
        return apiService.getSeriesGenres(BuildConfig.API_KEY)
            .subscribeOn(Schedulers.io())
            .flatMap { response ->
                return@flatMap requestSeriesFromNetwork(type, response.genres)
            }
    }

    private fun requestMoviesFromNetwork(
        type: String,
        listGenres: List<GenresResult>
    ): Flowable<List<MoviesResult>> {
        val service = when (type) {
            Constants.TYPE_MOVIES_NOW_PLAYING -> apiService.getMoviesNowPlaying(
                BuildConfig.API_KEY,
                1
            )
            Constants.TYPE_MOVIES_UPCOMING -> apiService.getMoviesUpcoming(BuildConfig.API_KEY, 1)
            Constants.TYPE_MOVIES_POPULAR -> apiService.getMoviesPopular(BuildConfig.API_KEY, 1)
            Constants.TYPE_MOVIES_TOP_RATED -> apiService.getMoviesTopRated(BuildConfig.API_KEY, 1)
            else -> null
        }
        return service
            ?.subscribeOn(Schedulers.io())
            ?.map { response ->
                response.results.forEach { item ->
                    item.genres = Constants.getGenres(listGenres, item.genreIds!!)
                    item.type = type
                }
                return@map response.results
            }!!
    }

    private fun requestSeriesFromNetwork(
        type: String,
        listGenres: List<GenresResult>
    ): Flowable<List<SeriesResult>> {
        val service = when (type) {
            Constants.TYPE_SERIES_TODAY_AIRING -> apiService.getSeriesTodayAiring(
                BuildConfig.API_KEY,
                1
            )
            Constants.TYPE_SERIES_ON_AIR -> apiService.getSeriesOnAir(BuildConfig.API_KEY, 1)
            Constants.TYPE_SERIES_POPULAR -> apiService.getSeriesPopular(BuildConfig.API_KEY, 1)
            Constants.TYPE_SERIES_TOP_RATED -> apiService.getSeriesTopRated(BuildConfig.API_KEY, 1)
            else -> null
        }
        return service
            ?.subscribeOn(Schedulers.io())
            ?.map { response ->
                response.results.forEach { item ->
                    item.genres = Constants.getGenres(listGenres, item.genreIds!!)
                    item.type = type
                }
                return@map response.results
            }!!
    }
}