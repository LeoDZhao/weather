package com.ebay.dozhao.myweatherapp

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.ebay.dozhao.myweatherapp.event.UpdateRecentSearchDoneEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        EventBus.getDefault().register(this)
    }

    override fun onResume() {
        super.onResume()
        RecentSearchRepository.updateRecentSearches()
        Log.d("dozhao", "onResume continures")
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUpdateRecentSearchDoneEvent(event: UpdateRecentSearchDoneEvent) {
        if (RecentSearchRepository.recentSearches.isNotEmpty()) {
            val intent = Intent(this, SearchResultActivity::class.java)
            intent.action = Intent.ACTION_SEARCH
            intent.putExtra(SearchManager.QUERY, RecentSearchRepository.recentSearches[0])
            startActivity(intent)
        } else {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }
}
