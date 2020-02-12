package com.homindolentrahar.meersani.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.homindolentrahar.meersani.ui.categories.DetailCategoriesViewModel
import com.homindolentrahar.meersani.ui.detail.DetailItemViewModel
import com.homindolentrahar.meersani.ui.genre.GenresViewModel
import com.homindolentrahar.meersani.ui.main.fragment.favorites.viewmodel.FavoritesMoviesViewModel
import com.homindolentrahar.meersani.ui.main.fragment.favorites.viewmodel.FavoritesSeriesViewModel
import com.homindolentrahar.meersani.ui.main.fragment.viewmodel.MoviesViewModel
import com.homindolentrahar.meersani.ui.main.fragment.viewmodel.SeriesViewModel
import com.homindolentrahar.meersani.ui.search.SearchViewModel
import com.homindolentrahar.meersani.util.ViewModelKey
import com.homindolentrahar.meersani.util.ViewModelProviderFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelProviderFactory(factory: ViewModelProviderFactory): ViewModelProvider.Factory

    //    Activity
    @Binds
    @IntoMap
    @ViewModelKey(DetailCategoriesViewModel::class)
    abstract fun bindDetailCategoriesViewModel(viewModel: DetailCategoriesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(viewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailItemViewModel::class)
    abstract fun bindDetailItemViewModel(viewModel: DetailItemViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GenresViewModel::class)
    abstract fun bindGenresViewModel(viewModel: GenresViewModel): ViewModel

    //    Fragment
    @Binds
    @IntoMap
    @ViewModelKey(MoviesViewModel::class)
    abstract fun bindMoviesViewModel(viewModel: MoviesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SeriesViewModel::class)
    abstract fun bindSeriesViewModel(viewModel: SeriesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoritesMoviesViewModel::class)
    abstract fun bindFavoritesMoviesViewModel(viewModel: FavoritesMoviesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoritesSeriesViewModel::class)
    abstract fun bindFavoritesSeriesViewModel(viewModel: FavoritesSeriesViewModel): ViewModel

}