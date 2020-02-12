package com.homindolentrahar.meersani.util

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.text.TextUtils
import android.view.View
import androidx.paging.PagedList
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.homindolentrahar.meersani.BuildConfig
import com.homindolentrahar.meersani.api.APIService
import com.homindolentrahar.meersani.model.GenresResult
import com.homindolentrahar.meersani.model.ProductionCompanies
import com.homindolentrahar.meersani.ui.categories.DetailCategoriesActivity
import com.homindolentrahar.meersani.ui.detail.DetailItemActivity
import com.homindolentrahar.meersani.ui.genre.GenresActivity
import com.homindolentrahar.meersani.ui.search.SearchActivity
import com.homindolentrahar.meersani.ui.settings.SettingsActivity
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.gpu.VignetteFilterTransformation
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.*

object Constants {
    const val DB_NAME = "localDatabase.db"
    const val CACHE_MOVIES_TABLE = "cache_movies_table"
    const val CACHE_SERIES_TABLE = "cache_series_table"
    const val FAVORITES_TABLE = "favorites_table"
    const val FAVORITES_MOVIES = "favorites_movies"
    const val FAVORITES_SERIES = "favorites_series"

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

    const val TYPE_MOVIES_BY_GENRE = "movies_by_genre"
    const val TYPE_SERIES_BY_GENRE = "series_by_genre"

    const val TYPE_PRIMARY_HOLDER = "primary_holder"
    const val TYPE_NORMAL_HOLDER = "normal_holder"

    const val NO_QUERY = "No Query"

    const val LANG_PREF = "lang_pref"

    fun getDefaultSharedPreference(context: Context): SharedPreferences {
        return context.getSharedPreferences(LANG_PREF, Context.MODE_PRIVATE)
    }

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

    fun navigateToGenres(context: Context, genresId: Int, name: String, type: String) {
        val intent = Intent(context, GenresActivity::class.java)
        intent.putExtra(GenresActivity.EXTRA_ID, genresId)
        intent.putExtra(GenresActivity.EXTRA_NAME, name)
        intent.putExtra(GenresActivity.EXTRA_TYPE, type)
        context.startActivity(intent)
    }

    fun navigateToSettings(context: Context) {
        val intent = Intent(context, SettingsActivity::class.java)
        context.startActivity(intent)
    }

    fun setProgressVisibility(loadingLayout: View, visibility: Int) {
        loadingLayout.visibility = visibility
    }

    fun getAPIService(): APIService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIService::class.java)
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

    fun getStringGenres(listGenres: List<GenresResult>): String {
        val genres = mutableListOf<String>()
        for (genre in listGenres) {
            genres.add(genre.name)
        }
        return TextUtils.join(", ", genres)
    }

    fun getFormattedReleaseDate(date: String): String {
        val currentDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(date)
        return DateTimeFormatter.ofPattern("MMM dd, yyyy").format(currentDate)
    }

    fun getFormattedCurrency(money: Long): String {
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