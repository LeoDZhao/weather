package com.ebay.dozhao.myweatherapp

import org.greenrobot.eventbus.EventBus
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherRepository {
    private var currentWeather: RawCurrentWeather? = null

    fun requestWeatherFromAPI(query: String) {
        if (query.isEmpty()) return
        val retrofit = Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val weatherService = retrofit.create(WeatherService::class.java)
        val response =
                if (query.first().isLetter()) weatherService.currentWeatherByCitiyName(query).execute()
                else weatherService.currentWeatherByZipCode(query).execute()

        val event = SearchDoneEvent()
        if (response.isSuccessful) {
            val body = response.body()
            body?.let {
                currentWeather = it
            }

        } else {
            event.message = "Error: " + response.code().toString() + "\n" + response.message()
        }

        EventBus.getDefault().post(event)
    }

    fun getCurrentWeather() = currentWeather
}