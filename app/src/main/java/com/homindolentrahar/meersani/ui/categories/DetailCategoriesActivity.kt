package com.homindolentrahar.meersani.ui.categories

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.homindolentrahar.meersani.R
import dagger.android.support.DaggerAppCompatActivity

class DetailCategoriesActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_categories)
    }
}
