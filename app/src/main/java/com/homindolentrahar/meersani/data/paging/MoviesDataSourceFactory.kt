package com.homindolentrahar.meersani.data.paging

import androidx.paging.DataSource
import com.homindolentrahar.meersani.api.APIService
import com.homindolentrahar.meersani.model.MoviesResult

class MoviesDataSourceFactory(
    private val apiService: APIService,
    private val type: String,
    private val query: String
) : DataSource.Factory<Int, MoviesResult>() {
    override fun create(): DataSource<Int, MoviesResult> {
        return MoviesDataSource(apiService, type, query)
    }
}