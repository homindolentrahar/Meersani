package com.homindolentrahar.meersani.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.homindolentrahar.meersani.model.Favorites
import com.homindolentrahar.meersani.model.MoviesResult
import com.homindolentrahar.meersani.model.SeriesResult
import com.homindolentrahar.meersani.util.Constants

@Database(
    entities = [MoviesResult::class, SeriesResult::class, Favorites::class],
    version = 1,
    exportSchema = false
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun moviesCacheDao(): MoviesCacheDao
    abstract fun seriesCacheDao(): SeriesCacheDao
    abstract fun favoritesDao(): FavoritesDao

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getDatabaseInstance(context: Context): LocalDatabase? {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context,
                            LocalDatabase::class.java,
                            Constants.DB_NAME
                        ).build()
                    }
                }
            }
            return INSTANCE
        }

    }
}