package com.homindolentrahar.meersani.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.lang.RuntimeException
import javax.inject.Inject
import javax.inject.Provider

class ViewModelProviderFactory @Inject constructor(private val creators: MutableMap<Class<out ViewModel?>?, Provider<ViewModel?>?>?) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var creator = creators!![modelClass]
        if (creator == null) {
            for (entry in creators.entries) {
                if (modelClass.isAssignableFrom(entry.key!!)) {
                    creator = entry.value
                }
            }
        }

        if (creator == null) {
            throw IllegalArgumentException("ViewModel class not found !")
        }

        try {
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}