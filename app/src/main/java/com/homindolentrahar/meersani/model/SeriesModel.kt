package com.homindolentrahar.meersani.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.homindolentrahar.meersani.util.Constants

data class SeriesResponse(
    @SerializedName("results")
    val results: List<SeriesResult>
)

@Entity(tableName = Constants.CACHE_SERIES_TABLE)
data class SeriesResult(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @Ignore
    @SerializedName("genre_ids")
    val genreIds: List<Int>?,
    @SerializedName("first_air_date")
    val firstAirDate: String,
    @SerializedName("vote_average")
    val rating: Double,
    @SerializedName("poster_path")
    val posterPath: String,
    var genres: String,
    var type: String
) {
    constructor(
        id: Int,
        name: String,
        firstAirDate: String,
        rating: Double,
        posterPath: String,
        genres: String,
        type: String
    ) :
            this(id, name, null, firstAirDate, rating, posterPath, genres, type)
}

data class SeriesDetail(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("first_air_date")
    val firstAirDate: String,
    @SerializedName("genres")
    val genres: List<GenresResult>,
    @SerializedName("vote_average")
    val rating: Double,
    @SerializedName("episode_run_time")
    val runtime: List<Int>,
    @SerializedName("number_of_episodes")
    val episodes: Int,
    @SerializedName("number_of_seasons")
    val seasons: Int,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompanies>,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("backdrop_path")
    val backdropPath: String
)