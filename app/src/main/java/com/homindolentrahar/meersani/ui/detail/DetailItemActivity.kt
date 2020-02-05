package com.homindolentrahar.meersani.ui.detail

import android.os.Bundle
import com.homindolentrahar.meersani.R
import dagger.android.support.DaggerAppCompatActivity

class DetailItemActivity : DaggerAppCompatActivity() {

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_TYPE = "extra_type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_item)
    }
}
