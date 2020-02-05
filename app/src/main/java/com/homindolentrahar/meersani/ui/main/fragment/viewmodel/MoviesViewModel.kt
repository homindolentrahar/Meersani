package com.homindolentrahar.meersani.ui.main.fragment.viewmodel

import androidx.lifecycle.ViewModel
import com.homindolentrahar.meersani.data.DataRepository
import com.homindolentrahar.meersani.model.MoviesResult
import io.reactivex.Flowable
import javax.inject.Inject

class MoviesViewModel @Inject constructor(private val repository: DataRepository) : ViewModel() {
    fun getCachedMovies(): Flowable<List<MoviesResult>> {
        return repository.getCachedMovies()
    }

    override fun onCleared() {
        super.onCleared()
        repository.clearDisposable()
    }
}