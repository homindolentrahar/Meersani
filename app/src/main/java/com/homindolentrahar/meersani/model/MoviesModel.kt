package com.homindolentrahar.meersani.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.homindolentrahar.meersani.util.Constants

data class MoviesResponse(
    @SerializedName("results")
    val results: List<MoviesResult>
)

@Entity(tableName = Constants.CACHE_MOVIES_TABLE)
data class MoviesResult(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @Ignore
    @SerializedName("genre_ids")
    val genreIds: List<Int>?,
    @SerializedName("release_date")
    val releaseDate: String = "No Date",
    @SerializedName("vote_average")
    val rating: Double,
    @SerializedName("poster_path")
    val posterPath: String,
    var genres: String,
    var type: String
) {
    constructor(
        id: Int,
        title: String,
        releaseDate: String,
        rating: Double,
        posterPath: String,
        genres: String,
        type: String
    ) :
            this(id, title, null, releaseDate, rating, posterPath, genres, type)
}

data class MoviesDetail(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("genres")
    val genres: List<GenresResult>,
    @SerializedName("vote_average")
    val rating: Double,
    @SerializedName("runtime")
    val runtime: Int,
    @SerializedName("budget")
    val budget: Long,
    @SerializedName("revenue")
    val revenue: Long,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompanies>,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("backdrop_path")
    val backdropPath: String
)