package com.ebay.dozhao.myweatherapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.greenrobot.eventbus.EventBus

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var presenter: SplashScreenActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        presenter = SplashScreenActivityPresenter(this)
        EventBus.getDefault().register(presenter)
    }

    override fun onResume() {
        super.onResume()
        presenter.updateRecentSearches()
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(presenter)
    }
}
