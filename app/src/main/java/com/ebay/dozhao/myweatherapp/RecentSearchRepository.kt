package com.ebay.dozhao.myweatherapp

import android.content.Context
import com.ebay.dozhao.myweatherapp.event.UpdateRecentSearchDoneEvent
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.Executors


object RecentSearchRepository {
    var recentSearches = ArrayList<String>()

    fun updateRecentSearches() {
        Executors.newSingleThreadExecutor().execute {
            val searches = getRecentSearchesStringInSharedPrefs()
            if (searches.isNotEmpty()) {
                recentSearches = searches.split(Constants.SAVED_RECENT_SEARCH_SEPERATOR).map { it.trim() } as ArrayList<String>
            }
            EventBus.getDefault().post(UpdateRecentSearchDoneEvent())
        }
    }

    fun saveRecentSearch(search: String) {
        if (!search.isEmpty()) {
            recentSearches.remove(search)
            recentSearches.add(0, search)
            val searches = recentSearches.joinToString(Constants.SAVED_RECENT_SEARCH_SEPERATOR)
            storeRecentSearchesStringInSharedPrefs(searches)
        }
    }

    private fun storeRecentSearchesStringInSharedPrefs(searches: String) {
        val sharedPreferences = MyApplication.instance!!.getSharedPreferences(Constants.SAVED_RECENT_SEARCH_SHARED_PREFS,
                Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(Constants.SAVED_RECENT_SEARCH_LIST, searches)
        editor.apply()
    }

    private fun getRecentSearchesStringInSharedPrefs(): String {
        val sharedPreferences = MyApplication.instance!!.getSharedPreferences(Constants.SAVED_RECENT_SEARCH_SHARED_PREFS,
                Context.MODE_PRIVATE)
        return sharedPreferences.getString(Constants.SAVED_RECENT_SEARCH_LIST, "")
    }
}