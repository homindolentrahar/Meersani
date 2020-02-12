package com.homindolentrahar.meersani.util

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import java.util.*

object LocaleManager {
    private val LANG_KEY = "lang_key"

    fun setLocale(context: Context): Context {
        val savedLanguage = getLanguage(context)
        return savedLanguage?.let { updateResources(context, it) } ?: context
    }

    fun setNewLocale(context: Context, language: String): Context {
        persistLanguage(context, language)
        return updateResources(context, language)
    }

    private fun getLanguage(context: Context): String? {
        val prefs = Constants.getDefaultSharedPreference(context)
        val currentLocale = getLocale(context.resources)
        return if (!prefs.contains(LANG_KEY)) null else prefs.getString(
            LANG_KEY,
            currentLocale.toString()
        )
    }

    private fun getLocale(resource: Resources): Locale {
        val config = resource.configuration
        return if (Build.VERSION.SDK_INT >= 24) config.locales.get(0) else config.locale
    }

    private fun persistLanguage(context: Context, language: String) {
        val pref = Constants.getDefaultSharedPreference(context)
        pref.edit().putString(LANG_KEY,language).apply()
    }

    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        return updateResources(context, locale)
    }

    @Suppress("DEPRECATION")
    private fun updateResources(context: Context, locale: Locale): Context {
        var ctx = context
        val resource = ctx.resources
        val config = Configuration(resource.configuration)

        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale)
            ctx = ctx.createConfigurationContext(config)
            resource.updateConfiguration(config, resource.displayMetrics)
        } else {
            config.locale = locale
            resource.updateConfiguration(config, resource.displayMetrics)
        }
        return ctx
    }
}