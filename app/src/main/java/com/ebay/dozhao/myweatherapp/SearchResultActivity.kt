package com.ebay.dozhao.myweatherapp

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

class SearchResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        // Get the intent, verify the action and get the query
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            doMySearch(query)
        }
    }

    private fun doMySearch(query: String) {
        val queryTextView: TextView = findViewById(R.id.query_text_view)
        queryTextView.text = query
    }
}
