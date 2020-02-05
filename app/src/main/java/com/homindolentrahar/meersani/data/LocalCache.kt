package com.homindolentrahar.meersani.data

import com.homindolentrahar.meersani.db.MoviesCacheDao
import com.homindolentrahar.meersani.db.SeriesCacheDao
import com.homindolentrahar.meersani.model.MoviesResult
import com.homindolentrahar.meersani.model.SeriesResult
import io.reactivex.Flowable
import javax.inject.Inject

class LocalCache @Inject constructor(
    private val moviesCacheDao: MoviesCacheDao,
    private val seriesCacheDao: SeriesCacheDao
) {
    fun getAllCachedMovies(): Flowable<List<MoviesResult>> {
        return moviesCacheDao.getAllCachedMovies()
    }

    fun getAllCachedSeries(): Flowable<List<SeriesResult>> {
        return seriesCacheDao.getAllCachedSeries()
    }

    fun insertAllMovies(list: List<MoviesResult>) {
        moviesCacheDao.insertAll(list)
    }

    fun insertAllSeries(list: List<SeriesResult>) {
        seriesCacheDao.insertAll(list)
    }
}