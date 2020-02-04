package com.homindolentrahar.meersani.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.homindolentrahar.meersani.model.MoviesResult
import com.homindolentrahar.meersani.model.SeriesResult
import io.reactivex.Flowable

@Dao
interface MoviesCacheDao {
    @Query("SELECT * FROM cache_movies_table")
    fun getAllCachedMovies(): Flowable<List<MoviesResult>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(list: List<MoviesResult>)
}

@Dao
interface SeriesCacheDao {
    @Query("SELECT * FROM cache_series_table")
    fun getAllCachedSeries(): Flowable<List<SeriesResult>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(list: List<SeriesResult>)
}