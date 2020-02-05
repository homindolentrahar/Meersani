package com.homindolentrahar.meersani.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.homindolentrahar.meersani.data.DataRepository
import com.homindolentrahar.meersani.model.MoviesResult
import com.homindolentrahar.meersani.model.SeriesResult
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val repository: DataRepository) : ViewModel() {
    fun searchMovies(query: String): LiveData<PagedList<MoviesResult>> {
        return repository.searchMovies(query)
    }

    fun searchSeries(query: String): LiveData<PagedList<SeriesResult>> {
        return repository.searchSeries(query)
    }
}