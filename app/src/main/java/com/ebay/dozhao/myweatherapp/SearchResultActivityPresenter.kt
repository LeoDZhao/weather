package com.ebay.dozhao.myweatherapp

import android.app.SearchManager
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.ebay.dozhao.myweatherapp.event.SearchDoneEvent
import com.ebay.dozhao.myweatherapp.raw.RawCurrentWeather
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

class SearchResultActivityPresenter(private val activity: SearchResultActivity) {

    fun processIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            val executor = Executors.newSingleThreadExecutor()
            executor.execute {
                WeatherRepository.requestWeatherFromAPI(query)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSearchDoneEvent(event: SearchDoneEvent) {
        if (event.message.isNotEmpty()) {
            Toast.makeText(activity, event.message, Toast.LENGTH_LONG).show()
            return
        }

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

            val simpleDateFormatDayLevel = SimpleDateFormat("yyyy/MM/dd", Locale.CANADA)
            val currentDateandTime = simpleDateFormatDayLevel.format(Date())
            val dateTextView: TextView = activity.findViewById(R.id.date)
            dateTextView.text = currentDateandTime

            val windDetailTextView: TextView = activity.findViewById(R.id.windDetail)
            windDetailTextView.text = it.wind.speed.toString()

            val cloudsDetailTextView: TextView = activity.findViewById(R.id.cloudinessDetail)
            cloudsDetailTextView.text = it.clouds.all.toString()

            val pressureDetailTextView: TextView = activity.findViewById(R.id.pressureDetail)
            pressureDetailTextView.text = it.mainAttribute.pressure.toString()

            val humidityTextView: TextView = activity.findViewById(R.id.humidityDetail)
            humidityTextView.text = it.mainAttribute.humidity.toString()

            val sunriseDate = Date(it.sys.sunrise)
            val sunriseTextView: TextView = activity.findViewById(R.id.sunriseDetail)
            sunriseTextView.text = sunriseDate.toString()

            val sunsetDate = Date(it.sys.sunset)
            val sunsetTextView: TextView = activity.findViewById(R.id.sunsetDetail)
            sunsetTextView.text = sunsetDate.toString()

            val geoCoordTextView: TextView = activity.findViewById(R.id.coordsDetail)
            val coords = "lat: ${it.coord.lat}, lon: ${it.coord.lon}"
            geoCoordTextView.text = coords

            val layout = activity.findViewById<View>(R.id.constraintLayout)
            layout.visibility = View.VISIBLE
        }

    }
}