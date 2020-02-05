package com.homindolentrahar.meersani.ui.detail

import androidx.lifecycle.ViewModel
import com.homindolentrahar.meersani.data.DataRepository
import com.homindolentrahar.meersani.model.DetailMovies
import com.homindolentrahar.meersani.model.DetailSeries
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
}