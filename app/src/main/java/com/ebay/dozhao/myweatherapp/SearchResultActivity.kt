package com.ebay.dozhao.myweatherapp

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.Executors


class SearchResultActivity : AppCompatActivity() {

    private var presenter: SearchResultActivityPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)
        presenter = SearchResultActivityPresenter(this)
        EventBus.getDefault().register(presenter)

        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            val executor = Executors.newSingleThreadExecutor()
            executor.execute {
                WeatherRepository.requestWeatherFromAPI(query)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(presenter)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = NavUtils.getParentActivityIntent(this)
        intent?.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
}
