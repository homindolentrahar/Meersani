package com.homindolentrahar.meersani.ui.genre

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.homindolentrahar.meersani.data.DataRepository
import com.homindolentrahar.meersani.model.MoviesResult
import com.homindolentrahar.meersani.model.SeriesResult
import javax.inject.Inject

class GenresViewModel @Inject constructor(private val repository: DataRepository) : ViewModel() {

    fun getMoviesByGenres(genresId: Int): LiveData<PagedList<MoviesResult>> {
        return repository.getMoviesByGenres(genresId)
    }

    fun getSeriesByGenres(genresId: Int): LiveData<PagedList<SeriesResult>> {
        return repository.getSeriesByGenres(genresId)
    }

}