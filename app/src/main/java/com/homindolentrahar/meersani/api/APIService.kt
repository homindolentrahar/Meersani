package com.homindolentrahar.meersani.api

import com.homindolentrahar.meersani.model.*
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    //    Movies
    @GET("movie/now_playing")
    fun getMoviesNowPlaying(
        @Query("api_key") key: String,
        @Query("page") page: Int
    ): Flowable<MoviesResponse>

    @GET("movie/upcoming")
    fun getMoviesUpcoming(
        @Query("api_key") key: String,
        @Query("page") page: Int
    ): Flowable<MoviesResponse>

    @GET("movie/popular")
    fun getMoviesPopular(
        @Query("api_key") key: String,
        @Query("page") page: Int
    ): Flowable<MoviesResponse>

    @GET("movie/top_rated")
    fun getMoviesTopRated(
        @Query("api_key") key: String,
        @Query("page") page: Int
    ): Flowable<MoviesResponse>

    @GET("movie/{movie_id}/recommendations")
    fun getMoviesRecommendations(
        @Path("movie_id") id: Int,
        @Query("api_key") key: String
    ): Flowable<MoviesResponse>

    @GET("search/movie")
    fun searchMovies(
        @Query("api_key") key: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): Flowable<MoviesResponse>

    @GET("movie/{movie_id}")
    fun getMoviesDetail(
        @Path("movie_id") id: Int,
        @Query("api_key") key: String
    ): Flowable<MoviesDetail>

    @GET("movie/{movie_id}/credits")
    fun getMoviesCast(
        @Path("movie_id") id: Int,
        @Query("api_key") key: String
    ): Flowable<CastResponse>

    @GET("genre/movie/list")
    fun getMoviesGenres(
        @Query("api_key") key: String
    ): Flowable<GenresResponse>

    //    Series
    @GET("tv/airing_today")
    fun getSeriesTodayAiring(
        @Query("api_key") key: String,
        @Query("page") page: Int
    ): Flowable<SeriesResponse>

    @GET("tv/on_the_air")
    fun getSeriesOnAir(
        @Query("api_key") key: String,
        @Query("page") page: Int
    ): Flowable<SeriesResponse>

    @GET("tv/popular")
    fun getSeriesPopular(
        @Query("api_key") key: String,
        @Query("page") page: Int
    ): Flowable<SeriesResponse>

    @GET("tv/top_rated")
    fun getSeriesTopRated(
        @Query("api_key") key: String,
        @Query("page") page: Int
    ): Flowable<SeriesResponse>

    @GET("tv/{tv_id}/recommendations")
    fun getSeriesRecommendations(
        @Path("tv_id") id: Int,
        @Query("api_key") key: String
    ): Flowable<SeriesResponse>

    @GET("search/tv")
    fun searchSeries(
        @Query("api_key") key: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): Flowable<SeriesResponse>

    @GET("tv/{tv_id}")
    fun getSeriesDetail(
        @Path("tv_id") id: Int,
        @Query("api_key") key: String
    ): Flowable<SeriesDetail>

    @GET("tv/{tv_id}/credits")
    fun getSeriesCast(
        @Path("tv_id") id: Int,
        @Query("api_key") key: String
    ): Flowable<CastResponse>

    @GET("genre/tv/list")
    fun getSeriesGenres(
        @Query("api_key") key: String
    ): Flowable<GenresResponse>

    //    Today Released Movies
    @GET("discover/movie")
    fun getTodayReleasedMovies(
        @Query("api_key") key: String,
        @Query("primary_release_date.gte") dateGte: String,
        @Query("primary_release_date.lte") dateLte: String
    ): Flowable<MoviesResponse>

    //    Get Items By Genres
    @GET("discover/movie")
    fun getMoviesByGenres(
        @Query("api_key") key: String,
        @Query("page") page: Int,
        @Query("with_genres") genresId: Int
    ): Flowable<MoviesResponse>

    @GET("discover/tv")
    fun getSeriesByGenres(
        @Query("api_key") key: String,
        @Query("page") page: Int,
        @Query("with_genres") genresId: Int
    ): Flowable<SeriesResponse>
}