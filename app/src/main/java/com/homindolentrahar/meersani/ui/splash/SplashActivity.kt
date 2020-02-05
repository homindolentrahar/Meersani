package com.homindolentrahar.meersani.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.homindolentrahar.meersani.R
import com.homindolentrahar.meersani.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
//        Loading to main page
        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
        }, 1000)
    }
}
