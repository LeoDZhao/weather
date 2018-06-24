package com.ebay.dozhao.myweatherapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import org.greenrobot.eventbus.EventBus


class SearchActivity : AppCompatActivity() {
    private var presenter: SearchActivityPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        presenter = SearchActivityPresenter(this)
        EventBus.getDefault().register(presenter)
        presenter?.cleanSearchView()
        val gpsLocationIcon = findViewById<View>(R.id.gps_locaiton_icon)
        gpsLocationIcon.setOnClickListener(presenter)
    }

    override fun onResume() {
        super.onResume()
        presenter?.cleanSearchView()
        presenter?.dynamicallyChangeVisibilityForRecentSearchLayout()
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(presenter)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        presenter?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
