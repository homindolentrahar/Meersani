package com.homindolentrahar.meersani.ui.main.fragment.favorites.viewmodel

import androidx.lifecycle.ViewModel
import com.homindolentrahar.meersani.data.DataRepository
import com.homindolentrahar.meersani.model.Favorites
import com.homindolentrahar.meersani.util.Constants
import io.reactivex.Flowable
import javax.inject.Inject

class FavoritesSeriesViewModel @Inject constructor(private val repository: DataRepository) :
    ViewModel() {
    fun getFavoritesSeries(): Flowable<List<Favorites>> {
        return repository.getFavoritesByType(Constants.FAVORITES_SERIES)
    }
}