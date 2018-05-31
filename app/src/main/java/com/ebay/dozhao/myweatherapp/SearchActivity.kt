package com.ebay.dozhao.myweatherapp

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.SearchView


class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setACleanSearchView()
    }

    override fun onResume() {
        super.onResume()
        setACleanSearchView()
    }

    private fun setACleanSearchView() {
        val searchView: SearchView = findViewById(R.id.search_view)
        val searchManager: SearchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val componentName = ComponentName(this, SearchResultActivity::class.java)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setQuery("", false)
        searchView.queryHint = getString(R.string.search_hint)
        searchView.setIconifiedByDefault(false)
        searchView.clearFocus()
    }
}
