package com.homindolentrahar.meersani.data.paging

import androidx.paging.DataSource
import com.homindolentrahar.meersani.api.APIService
import com.homindolentrahar.meersani.model.SeriesResult

class SeriesDataSourceFactory(
    private val apiService: APIService,
    private val type: String,
    private val query: String
) : DataSource.Factory<Int, SeriesResult>() {
    override fun create(): DataSource<Int, SeriesResult> {
        return SeriesDataSource(apiService, type, query)
    }
}