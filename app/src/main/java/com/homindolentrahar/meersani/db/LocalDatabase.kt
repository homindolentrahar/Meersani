package com.homindolentrahar.meersani.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.homindolentrahar.meersani.model.MoviesResult
import com.homindolentrahar.meersani.model.SeriesResult

@Database(entities = [MoviesResult::class, SeriesResult::class], version = 1, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun moviesCacheDao(): MoviesCacheDao
    abstract fun seriesCacheDao(): SeriesCacheDao
}