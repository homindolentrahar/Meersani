package com.homindolentrahar.meersani.worker

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.homindolentrahar.meersani.BuildConfig
import com.homindolentrahar.meersani.api.APIService
import com.homindolentrahar.meersani.db.LocalDatabase
import com.homindolentrahar.meersani.model.GenresResult
import com.homindolentrahar.meersani.model.MoviesResult
import com.homindolentrahar.meersani.model.SeriesResult
import com.homindolentrahar.meersani.util.Constants
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class SyncWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    private val apiService = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(APIService::class.java)

    private val moviesCacheDao = LocalDatabase.getDatabaseInstance(context)?.moviesCacheDao()
    private val seriesCacheDao = LocalDatabase.getDatabaseInstance(context)?.seriesCacheDao()
    private var resultStatus: Result? = null

    override fun doWork(): Result {
        return if (cachingMoviesToDatabase() == Result.success() && cachingSeriesToDatabase() == Result.success()) {
            Result.success()
        } else {
            Result.failure()
        }
    }

    @SuppressLint("CheckResult")
    private fun cachingMoviesToDatabase(): Result {
        combineRequestMovies()
            .subscribeOn(Schedulers.io())
            .subscribe(
                { list ->
                    moviesCacheDao?.deleteAll()
                    moviesCacheDao?.insertAll(list)
                    resultStatus = Result.success()
                },
                { error ->
                    resultStatus = Result.failure()
                }
            )
        return resultStatus as Result
    }

    @SuppressLint("CheckResult")
    private fun cachingSeriesToDatabase(): Result {
        combineRequestSeries()
            .subscribeOn(Schedulers.io())
            .subscribe(
                { list ->
                    seriesCacheDao?.deleteAll()
                    seriesCacheDao?.insertAll(list)
                    resultStatus = Result.success()
                },
                { error ->
                    resultStatus = Result.failure()
                }
            )
        return resultStatus as Result
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