package com.homindolentrahar.meersani.di.module

import android.app.Application
import androidx.room.Room
import com.homindolentrahar.meersani.BuildConfig
import com.homindolentrahar.meersani.api.APIService
import com.homindolentrahar.meersani.data.DataRepository
import com.homindolentrahar.meersani.data.LocalCache
import com.homindolentrahar.meersani.db.FavoritesDao
import com.homindolentrahar.meersani.db.LocalDatabase
import com.homindolentrahar.meersani.db.MoviesCacheDao
import com.homindolentrahar.meersani.db.SeriesCacheDao
import com.homindolentrahar.meersani.util.Constants
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object AppModule {
    @Singleton
    @JvmStatic
    @Provides
    fun provideApiService(): APIService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIService::class.java)
    }

    @Singleton
    @JvmStatic
    @Provides
    fun provideLocalDatabase(application: Application): LocalDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            LocalDatabase::class.java,
            Constants.DB_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @JvmStatic
    @Provides
    fun provideMoviesCacheDao(database: LocalDatabase): MoviesCacheDao {
        return database.moviesCacheDao()
    }

    @Singleton
    @JvmStatic
    @Provides
    fun provideSeriesCacheDao(database: LocalDatabase): SeriesCacheDao {
        return database.seriesCacheDao()
    }

    @Singleton
    @JvmStatic
    @Provides
    fun provideFavoritesDao(database: LocalDatabase): FavoritesDao {
        return database.favoritesDao()
    }

    @Singleton
    @JvmStatic
    @Provides
    fun provideLocalCache(
        moviesCacheDao: MoviesCacheDao,
        seriesCacheDao: SeriesCacheDao
    ): LocalCache {
        return LocalCache(moviesCacheDao, seriesCacheDao)
    }

    @Singleton
    @JvmStatic
    @Provides
    fun provideDataRepository(
        apiService: APIService,
        localCache: LocalCache,
        favoritesDao: FavoritesDao
    ): DataRepository {
        return DataRepository(apiService, localCache, favoritesDao)
    }
}