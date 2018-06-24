package com.ebay.dozhao.myweatherapp

import com.ebay.dozhao.myweatherapp.event.SearchDoneEvent
import com.ebay.dozhao.myweatherapp.raw.RawCurrentWeather
import org.greenrobot.eventbus.EventBus
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Pattern

object WeatherRepository {
    private var currentWeather: RawCurrentWeather? = null

    fun requestWeatherFromAPI(query: String) {
        if (query.isEmpty()) return
        val retrofit = Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val weatherService = retrofit.create(WeatherService::class.java)
        var response: Response<RawCurrentWeather>? = null

        val regexForDouble = "-?\\d+\\.?\\d*"
        if (query.matches("^lat=$regexForDouble&lon=$regexForDouble$".toRegex())) {
            val pattern = Pattern.compile(regexForDouble)
            val matcher = pattern.matcher(query)
            var lat = ""
            var lon = ""
            if (matcher.find()) {
                lat = matcher.group(0)
            }
            if (matcher.find()) {
                lon = matcher.group(0)
                response = weatherService.currentWeatherByLatLon(lat, lon).execute()
            }
        } else if (query.matches("^\\d+(?:[-\\s]\\d+)?\$".toRegex())) {
            response = weatherService.currentWeatherByZipCode(query).execute()
        } else {
            response = weatherService.currentWeatherByCityName(query).execute()
        }

        val event = SearchDoneEvent()
        when {
            response == null -> event.message = "Error: response is null"
            response.isSuccessful -> {
                val body = response.body()
                body?.let {
                    currentWeather = it
                }
                RecentSearchRepository.addRecentSearch(query)
            }
            else -> event.message = "Error: " + response.code().toString() + "\n" + response.message()
        }
        EventBus.getDefault().post(event)
    }

    fun getCurrentWeather() = currentWeather
}