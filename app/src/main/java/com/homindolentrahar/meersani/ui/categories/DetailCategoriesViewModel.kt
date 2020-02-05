package com.homindolentrahar.meersani.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.homindolentrahar.meersani.data.DataRepository
import com.homindolentrahar.meersani.model.MoviesResult
import com.homindolentrahar.meersani.model.SeriesResult
import javax.inject.Inject

class DetailCategoriesViewModel @Inject constructor(private val repository: DataRepository) :
    ViewModel() {

    fun getPagedMovies(type: String): LiveData<PagedList<MoviesResult>> {
        return repository.getPagedMovies(type)
    }

    fun getPagedSeries(type: String): LiveData<PagedList<SeriesResult>> {
        return repository.getPagedSeries(type)
    }

}