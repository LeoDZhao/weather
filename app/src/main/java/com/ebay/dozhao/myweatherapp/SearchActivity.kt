package com.ebay.dozhao.myweatherapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity


class SearchActivity : AppCompatActivity() {
    private var presenter: SearchActivityPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        presenter = SearchActivityPresenter(this)
        presenter?.cleanSearchView()
    }

    override fun onResume() {
        super.onResume()
        presenter?.cleanSearchView()
        presenter?.setVisibilityForRecentSearch()
    }
}
