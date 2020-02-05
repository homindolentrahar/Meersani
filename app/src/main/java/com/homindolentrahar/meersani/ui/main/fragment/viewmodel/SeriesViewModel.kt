package com.homindolentrahar.meersani.ui.main.fragment.viewmodel

import androidx.lifecycle.ViewModel
import com.homindolentrahar.meersani.data.DataRepository
import com.homindolentrahar.meersani.model.SeriesResult
import io.reactivex.Flowable
import javax.inject.Inject

class SeriesViewModel @Inject constructor(private val repository: DataRepository) : ViewModel() {
    fun getCachedSeries(): Flowable<List<SeriesResult>> {
        return repository.getCachedSeries()
    }

    override fun onCleared() {
        super.onCleared()
        repository.clearDisposable()
    }
}