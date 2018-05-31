package com.ebay.dozhao.myweatherapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


    }

    override fun onResume() {
        super.onResume()
        nextActivity()
    }

    private fun nextActivity() {
        //if there is a recent search, show detail weather page
        SystemClock.sleep(3000)
        //else show search page
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }
}
