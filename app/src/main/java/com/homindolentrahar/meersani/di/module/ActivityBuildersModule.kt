package com.homindolentrahar.meersani.di.module

import com.homindolentrahar.meersani.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity
}