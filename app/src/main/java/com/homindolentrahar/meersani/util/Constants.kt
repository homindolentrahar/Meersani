package com.homindolentrahar.meersani.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.text.TextUtils
import android.view.View
import androidx.paging.PagedList
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.homindolentrahar.meersani.model.GenresResult
import com.homindolentrahar.meersani.model.ProductionCompanies
import com.homindolentrahar.meersani.ui.categories.DetailCategoriesActivity
import com.homindolentrahar.meersani.ui.detail.DetailItemActivity
import com.homindolentrahar.meersani.ui.search.SearchActivity
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.gpu.VignetteFilterTransformation
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.*

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

    const val TYPE_SEARCH_MOVIES = "search_movies"
    const val TYPE_SEARCH_SERIES = "search_series"

    const val TYPE_PRIMARY_HOLDER = "primary_holder"
    const val TYPE_NORMAL_HOLDER = "normal_holder"
    const val TYPE_PAGED_HOLDER = "paged_holder"

    const val NO_QUERY = "No Query"

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

    fun navigateToCategories(context: Context, type: String) {
        val intent = Intent(context, DetailCategoriesActivity::class.java)
        intent.putExtra(DetailCategoriesActivity.EXTRA_TYPE, type)
        context.startActivity(intent)
    }

    fun setProgressVisibility(loadingLayout: View, visibility: Int) {
        loadingLayout.visibility = visibility
    }

    fun getPagedListConfig(pageSize: Int): PagedList.Config {
        return PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 10)
            .setEnablePlaceholders(true)
            .build()
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

    fun getFormattedReleaseDate(date: String): String {
        val currentDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(date)
        return DateTimeFormatter.ofPattern("MMM dd, yyyy").format(currentDate)
    }

    fun getFormattedCurrency(money: Int): String {
        val currencyFormatter = NumberFormat.getCurrencyInstance()
        currencyFormatter.maximumFractionDigits = 0
        currencyFormatter.currency = Currency.getInstance("USD")
        return currencyFormatter.format(money)
    }

    fun getReleasedYear(releaseDate: String): String {
        return releaseDate.split("-")[0]
    }

    fun getEpisodesRuntime(listRuntime: List<Int>): String {
        val runtimes = mutableListOf<String>()
        for (runtime in listRuntime) {
            runtimes.add(runtime.toString())
        }
        return TextUtils.join(", ", runtimes)
    }

    fun getProductionHouse(list: List<ProductionCompanies>): String {
        val ph = mutableListOf<String>()
        for (production in list) {
            ph.add(production.name)
        }
        return TextUtils.join(", ", ph)
    }
}