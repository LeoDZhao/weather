package com.ebay.dozhao.myweatherapp

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.view.View
import android.widget.SearchView

class SearchActivityPresenter(private val activity: SearchActivity) {

    fun cleanSearchView() {
        val searchView: SearchView = activity.findViewById(R.id.search_view)
        val searchManager: SearchManager = activity.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val componentName = ComponentName(activity, SearchResultActivity::class.java)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setQuery("", false)
        searchView.queryHint = activity.getString(R.string.search_hint)
        searchView.setIconifiedByDefault(false)
        searchView.clearFocus()
    }

    fun setVisibilityForRecentSearch() {
        val recentSearchLayout = activity.findViewById<View>(R.id.recent_search_layout)
        if (RecentSearchRepository.recentSearches.isEmpty()) {
            recentSearchLayout.visibility = View.GONE
        } else {
            recentSearchLayout.visibility = View.VISIBLE
        }
    }
}