package com.ebay.dozhao.myweatherapp

import java.util.concurrent.Executors
import org.greenrobot.eventbus.EventBus



object RecentSearchRepository {
    fun getRecentSearches() {
        Executors.newSingleThreadExecutor().execute {
            Thread.sleep(2000)
            EventBus.getDefault().post(GetRecentSearchDoneEvent())
        }
    }
}