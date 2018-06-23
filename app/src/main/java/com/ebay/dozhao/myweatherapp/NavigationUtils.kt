package com.ebay.dozhao.myweatherapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent

object NavigationUtils {
    fun startSearchResultActivity(context: Context, query: String) {
        val intent = Intent(context, SearchResultActivity::class.java)
        intent.action = Intent.ACTION_SEARCH
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.putExtra(SearchManager.QUERY, query)
        context.startActivity(intent)
    }
}