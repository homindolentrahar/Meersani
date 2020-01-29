package com.homindolentrahar.meersani.di.module

import androidx.lifecycle.ViewModelProvider
import com.homindolentrahar.meersani.util.ViewModelKey
import com.homindolentrahar.meersani.util.ViewModelProviderFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelProviderFactory(factory: ViewModelProviderFactory): ViewModelProvider.Factory

}