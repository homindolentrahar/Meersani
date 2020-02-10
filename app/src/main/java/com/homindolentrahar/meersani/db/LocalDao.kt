package com.homindolentrahar.meersani.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.homindolentrahar.meersani.model.Favorites
import com.homindolentrahar.meersani.model.MoviesResult
import com.homindolentrahar.meersani.model.SeriesResult
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface MoviesCacheDao {
    @Query("SELECT * FROM cache_movies_table")
    fun getAllCachedMovies(): Flowable<List<MoviesResult>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(list: List<MoviesResult>)

    @Query("DELETE FROM cache_movies_table")
    fun deleteAll()
}

@Dao
interface SeriesCacheDao {
    @Query("SELECT * FROM cache_series_table")
    fun getAllCachedSeries(): Flowable<List<SeriesResult>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(list: List<SeriesResult>)

    @Query("DELETE FROM cache_series_table")
    fun deleteAll()
}

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM favorites_table WHERE itemId = :itemId")
    fun checkExistedItem(itemId: Int): Flowable<List<Favorites>>

    @Query("SELECT * FROM favorites_table WHERE type =:type")
    fun getFavoritesByType(type: String): Flowable<List<Favorites>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Favorites): Completable

    @Query("DELETE FROM favorites_table WHERE itemId =:itemId")
    fun delete(itemId: Int): Completable
}