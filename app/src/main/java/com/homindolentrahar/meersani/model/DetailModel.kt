package com.homindolentrahar.meersani.model

data class DetailMovies(
    val moviesDetail: MoviesDetail,
    val cast: CastResponse,
    val moviesRecommendations: List<MoviesResult>
)

data class DetailSeries(
    val seriesDetail: SeriesDetail,
    val cast: CastResponse,
    val seriesRecommendations: List<SeriesResult>
)