package com.homindolentrahar.meersani.di.module

import com.homindolentrahar.meersani.ui.categories.DetailCategoriesActivity
import com.homindolentrahar.meersani.ui.detail.DetailItemActivity
import com.homindolentrahar.meersani.ui.genre.GenresActivity
import com.homindolentrahar.meersani.ui.main.MainActivity
import com.homindolentrahar.meersani.ui.search.SearchActivity
import com.homindolentrahar.meersani.ui.settings.SettingsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeDetailCategoriesActivity(): DetailCategoriesActivity

    @ContributesAndroidInjector
    abstract fun contributeDetailItemActivity(): DetailItemActivity

    @ContributesAndroidInjector
    abstract fun contributeSearchActivity(): SearchActivity

    @ContributesAndroidInjector
    abstract fun contributeSettingsActivity(): SettingsActivity

    @ContributesAndroidInjector
    abstract fun contributeGenresActivity(): GenresActivity
}