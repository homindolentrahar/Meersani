package com.homindolentrahar.meersani

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import androidx.work.*
import com.homindolentrahar.meersani.util.LocaleManager
import com.homindolentrahar.meersani.worker.SyncWorker
import dagger.android.support.DaggerAppCompatActivity
import java.util.concurrent.TimeUnit

open class BaseActivity : DaggerAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        syncData()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        LocaleManager.setLocale(newBase!!)
    }

    private fun syncData() {
        val constraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresDeviceIdle(true)
            .build()
        val syncRequest = PeriodicWorkRequest.Builder(SyncWorker::class.java, 1, TimeUnit.DAYS)
            .setConstraints(constraint)
            .build()
        WorkManager.getInstance(applicationContext).enqueue(syncRequest)
    }
}