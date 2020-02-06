package com.homindolentrahar.meersani.ui.detail

import androidx.lifecycle.ViewModel
import com.homindolentrahar.meersani.data.DataRepository
import com.homindolentrahar.meersani.model.DetailMovies
import com.homindolentrahar.meersani.model.DetailSeries
import com.homindolentrahar.meersani.model.Favorites
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

class DetailItemViewModel @Inject constructor(private val repository: DataRepository) :
    ViewModel() {
    fun getDetailMovies(id: Int): Flowable<DetailMovies> {
        return repository.getDetailMovies(id)
    }

    fun getDetailSeries(id: Int): Flowable<DetailSeries> {
        return repository.getDetailSeries(id)
    }

    fun checkExistedItem(itemId: Int): Flowable<List<Favorites>> {
        return repository.checkExistedItem(itemId)
    }

    fun insert(item: Favorites): Completable {
        return repository.insert(item)
    }

    fun delete(itemId: Int): Completable {
        return repository.delete(itemId)
    }
}