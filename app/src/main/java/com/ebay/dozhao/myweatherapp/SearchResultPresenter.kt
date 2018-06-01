package com.ebay.dozhao.myweatherapp

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*

class SearchResultPresenter(val activity: SearchResultActivity) {

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSearchDoneEvent(event: SearchDoneEvent) {
        val progressBar: View = activity.findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE

        val currentWeather: RawCurrentWeather? = WeatherRepository.getCurrentWeather()
        currentWeather?.let {
            val titleTextView: TextView = activity.findViewById(R.id.title)
            titleTextView.text = "Weather in ${it.name}"

            val firstWeather = it.weatherList.firstOrNull()
            firstWeather?.let {
                val weatherIcon: ImageView = activity.findViewById(R.id.icon)
                Glide.with(activity)
                        .load("http://openweathermap.org/img/w/${it.icon}.png")
                        .into(weatherIcon)

                val mainWeatherTextView: TextView = activity.findViewById(R.id.mainWeather)
                mainWeatherTextView.text = it.main
            }

            val temperatureTextView: TextView = activity.findViewById(R.id.temperature)
            temperatureTextView.text = it.mainAttribute.temp.toString()

            val sdf = SimpleDateFormat("yyyy/MM/dd")
            val currentDateandTime = sdf.format(Date())
            val dateTextView: TextView = activity.findViewById(R.id.date)
            dateTextView.text = currentDateandTime

        }

    }
}