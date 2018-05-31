package com.ebay.dozhao.myweatherapp

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_search_result.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.Executors

class SearchResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)
        EventBus.getDefault().register(this)

        // Get the intent, verify the action and get the query
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            doMySearch(query)
        }
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    private fun doMySearch(query: String) {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            //do search
            Thread.sleep(2000)
            val event = SearchDoneEvent()
            event.query = query
            EventBus.getDefault().post(event)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSearchDoneEvent(event: SearchDoneEvent) {
        progressBar.visibility = View.GONE
        queryTextView.text = event.query
        queryTextView.visibility = View.VISIBLE
    }


}
