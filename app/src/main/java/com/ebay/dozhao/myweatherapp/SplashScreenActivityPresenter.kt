package com.ebay.dozhao.myweatherapp

import android.content.Intent
import com.ebay.dozhao.myweatherapp.event.UpdateRecentSearchDoneEvent
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class SplashScreenActivityPresenter(private val activity: SplashScreenActivity) {

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUpdateRecentSearchDoneEvent(event: UpdateRecentSearchDoneEvent) {
        val recentSearches = RecentSearchRepository.recentSearches
        if (recentSearches.isNotEmpty()) {
            NavigationUtils.startSearchResultActivity(activity, recentSearches[0])
        } else {
            val intent = Intent(activity, SearchActivity::class.java)
            activity.startActivity(intent)
        }
    }

    fun updateRecentSearches() {
        RecentSearchRepository.updateRecentSearchesFromDeviceStorage()
    }
}