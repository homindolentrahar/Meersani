package com.homindolentrahar.meersani.di.module

import com.homindolentrahar.meersani.ui.main.fragment.FavoritesFragment
import com.homindolentrahar.meersani.ui.main.fragment.MoviesFragment
import com.homindolentrahar.meersani.ui.main.fragment.SeriesFragment
import com.homindolentrahar.meersani.ui.main.fragment.favorites.FavoritesMoviesFragment
import com.homindolentrahar.meersani.ui.main.fragment.favorites.FavoritesSeriesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeMoviesFragment(): MoviesFragment

    @ContributesAndroidInjector
    abstract fun contributeSeriesFragment(): SeriesFragment

    @ContributesAndroidInjector
    abstract fun contributeFavoritesFragment(): FavoritesFragment

    @ContributesAndroidInjector
    abstract fun contributeFavoritesMoviesFragment(): FavoritesMoviesFragment

    @ContributesAndroidInjector
    abstract fun contributeFavoritesSeriesFragment(): FavoritesSeriesFragment
}