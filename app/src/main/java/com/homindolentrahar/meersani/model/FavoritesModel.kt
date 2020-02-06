package com.homindolentrahar.meersani.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.homindolentrahar.meersani.util.Constants

@Entity(tableName = Constants.FAVORITES_TABLE)
data class Favorites(
    @PrimaryKey(autoGenerate = false)
    val id: Int?,
    val itemId: Int,
    val title: String,
    val release: String,
    val genres: String,
    val rating: Double,
    val posterPath: String,
    val type: String
) {
    constructor(
        itemId: Int,
        title: String,
        release: String,
        genres: String,
        rating: Double,
        posterPath: String,
        type: String
    ) :
            this(null, itemId, title, release, genres, rating, posterPath, type)
}