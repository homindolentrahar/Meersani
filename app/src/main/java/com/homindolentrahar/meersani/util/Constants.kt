package com.homindolentrahar.meersani.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.text.TextUtils
import android.view.View
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.homindolentrahar.meersani.model.GenresResult
import com.homindolentrahar.meersani.ui.detail.DetailItemActivity
import com.homindolentrahar.meersani.ui.search.SearchActivity
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.gpu.VignetteFilterTransformation

object Constants {
    const val DB_NAME = "localDatabase.db"
    const val CACHE_MOVIES_TABLE = "cache_movies_table"
    const val CACHE_SERIES_TABLE = "cache_series_table"

    const val TYPE_MOVIES_NOW_PLAYING = "movies_now_playing"
    const val TYPE_MOVIES_UPCOMING = "movies_upcoming"
    const val TYPE_MOVIES_POPULAR = "movies_popular"
    const val TYPE_MOVIES_TOP_RATED = "movies_top_rated"

    const val TYPE_SERIES_TODAY_AIRING = "series_today_airing"
    const val TYPE_SERIES_ON_AIR = "series_on_air"
    const val TYPE_SERIES_POPULAR = "series_popular"
    const val TYPE_SERIES_TOP_RATED = "series_top_rated"

    const val TYPE_PRIMARY_HOLDER = "primary_holder"
    const val TYPE_NORMAL_HOLDER = "normal_holder"
    const val TYPE_PAGED_HOLDER = "paged_holder"

    fun navigateToDetailItem(context: Context, id: Int, type: String) {
        val intent = Intent(context, DetailItemActivity::class.java)
        intent.putExtra(DetailItemActivity.EXTRA_ID, id)
        intent.putExtra(DetailItemActivity.EXTRA_TYPE, type)
        context.startActivity(intent)
    }

    fun navigateToSearch(context: Context, type: String) {
        val intent = Intent(context, SearchActivity::class.java)
        intent.putExtra(SearchActivity.EXTRA_TYPE, type)
        context.startActivity(intent)
    }

    fun setProgressVisibility(loadingLayout: View, visibility: Int) {
        loadingLayout.visibility = visibility
    }

    fun getBannerOptions(): Transformation<Bitmap> {
        return MultiTransformation<Bitmap>(
            BlurTransformation(18),
            VignetteFilterTransformation()
        )
    }

    fun getGenres(listGenres: List<GenresResult>, genreIds: List<Int>): String {
        val genres = mutableListOf<String>()
        for (id in genreIds) {
            for (genre in listGenres) {
                if (id == genre.id) {
                    genres.add(genre.name)
                }
            }
        }
        return TextUtils.join(", ", genres)
    }

    fun getReleasedYear(releaseDate: String): String {
        return if (releaseDate != "" && releaseDate.split("-").isNotEmpty()) releaseDate.split("-")[0] else "No Date Release"
    }
}