package com.homindolentrahar.meersani.ui.settings

import android.os.Bundle
import com.homindolentrahar.meersani.BaseActivity
import com.homindolentrahar.meersani.R
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
//        Inflate Setting Preference
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SettingsPreferenceFragment()).commit()

        btn_back.setOnClickListener { finish() }
    }
}
