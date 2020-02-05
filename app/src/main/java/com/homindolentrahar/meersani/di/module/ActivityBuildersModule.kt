package com.homindolentrahar.meersani.di.module

import com.homindolentrahar.meersani.ui.categories.DetailCategoriesActivity
import com.homindolentrahar.meersani.ui.detail.DetailItemActivity
import com.homindolentrahar.meersani.ui.main.MainActivity
import com.homindolentrahar.meersani.ui.search.SearchActivity
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
}