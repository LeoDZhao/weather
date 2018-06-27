package com.ebay.dozhao.myweatherapp

import android.app.SearchManager
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ebay.dozhao.myweatherapp.event.SearchDoneEvent
import com.ebay.dozhao.myweatherapp.raw.RawCurrentWeather
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

class SearchResultActivityPresenter(private val activity: SearchResultActivity) {

    private val progressBar: View = activity.findViewById(R.id.progressBar)
    private val weatherDetailLayout: View = activity.findViewById(R.id.weather_detail_layout)
    private val errorMessage = activity.findViewById<TextView>(R.id.error_message)
    private var currentWeather: RawCurrentWeather? = null


    fun processIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            val executor = Executors.newSingleThreadExecutor()
            executor.execute {
                WeatherRepository.requestWeatherFromAPI(query)
            }
            hideWeatherDetailLayout()
            showProgressBar()
        }
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun showWeatherDetailLayout() {
        weatherDetailLayout.visibility = View.VISIBLE
    }

    private fun hideWeatherDetailLayout() {
        weatherDetailLayout.visibility = View.GONE
    }

    private fun showErrorMessage() {
        errorMessage.visibility = View.VISIBLE
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSearchDoneEvent(event: SearchDoneEvent) {
        if (event.errorMessage.isNotEmpty()) {
            hideProgressBar()
            errorMessage.text = event.errorMessage
            showErrorMessage()
            return
        }

        currentWeather = WeatherRepository.getCurrentWeather()
        currentWeather?.let {
            val titleTextView: TextView = activity.findViewById(R.id.title)
            var title = it.name
            if (title.isEmpty()) {
                title = activity.resources.getString(R.string.unknown)
            }
            titleTextView.text = title

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
            val temp = BigDecimal.valueOf(it.mainAttribute.temp - 273.15).setScale(2, RoundingMode.HALF_UP)
            temperatureTextView.text = activity.resources.getString(R.string.temperature, temp)

            val simpleDateFormatSecondLevel = SimpleDateFormat("yyyy/MM/dd    HH:mm:ss", Locale.getDefault())
            val currentDateTime = simpleDateFormatSecondLevel.format(Date())
            val dateTextView: TextView = activity.findViewById(R.id.date)
            dateTextView.text = currentDateTime

            val windDetailTextView: TextView = activity.findViewById(R.id.windDetail)
            windDetailTextView.text = it.wind.speed.toString()

            val cloudsDetailTextView: TextView = activity.findViewById(R.id.cloudinessDetail)
            cloudsDetailTextView.text = it.clouds.all.toString()

            val pressureDetailTextView: TextView = activity.findViewById(R.id.pressureDetail)
            pressureDetailTextView.text = it.mainAttribute.pressure.toString()

            val humidityTextView: TextView = activity.findViewById(R.id.humidityDetail)
            humidityTextView.text = it.mainAttribute.humidity.toString()

            val sunriseDate = Date(it.sys.sunrise * 1000)
            val sunriseTextView: TextView = activity.findViewById(R.id.sunriseDetail)
            sunriseTextView.text = sunriseDate.toString()

            val sunsetDate = Date(it.sys.sunset * 1000)
            val sunsetTextView: TextView = activity.findViewById(R.id.sunsetDetail)
            sunsetTextView.text = sunsetDate.toString()

            val geoCoordTextView: TextView = activity.findViewById(R.id.coordsDetail)
            val coords = "lat: ${it.coord.lat}, lon: ${it.coord.lon}"
            geoCoordTextView.text = coords
        }
        hideProgressBar()
        showWeatherDetailLayout()
    }
}