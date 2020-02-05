package com.homindolentrahar.meersani.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.homindolentrahar.meersani.ui.main.fragment.viewmodel.MoviesViewModel
import com.homindolentrahar.meersani.ui.main.fragment.viewmodel.SeriesViewModel
import com.homindolentrahar.meersani.util.ViewModelKey
import com.homindolentrahar.meersani.util.ViewModelProviderFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelProviderFactory(factory: ViewModelProviderFactory): ViewModelProvider.Factory

    //    Fragment
    @Binds
    @IntoMap
    @ViewModelKey(MoviesViewModel::class)
    abstract fun bindMoviesViewModel(viewModel: MoviesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SeriesViewModel::class)
    abstract fun bindSeriesViewModel(viewModel: SeriesViewModel): ViewModel

}