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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
            val retrofit = Retrofit.Builder()
                    .baseUrl("http://api.openweathermap.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            val weatherService = retrofit.create(WeatherService::class.java)
            val response = weatherService.currentWeatherByCitiyName(query).execute()

            val event = SearchDoneEvent()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    event.query = body.id.toString() + "\n" +
                            (body.name) + "\n" +
                            body.cod + "\n" +
                            body.coord.lon + ", " + body.coord.lat + "\n" +
                            body.weatherList.size + "\n" +
                            "${body.weatherList[0].id}, ${body.weatherList[0].main}, ${body.weatherList[0].description}, ${body.weatherList[0].icon}"
                }

            } else {
                event.query = "Error: " + response.code().toString() + "\n" + response.message()
            }

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
