package com.ebay.dozhao.myweatherapp

import org.greenrobot.eventbus.EventBus
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherRepository {
    private var currentWeather: RawCurrentWeather? = null

    fun requestWeatherFromAPI(query: String) {
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
                currentWeather = it
//                event.message = body.id.toString() + "\n" +
//                        (body.name) + "\n" +
//                        body.cod + "\n" +
//                        body.coord.lon + ", " + body.coord.lat + "\n" +
//                        body.weatherList.size + "\n" +
//                        "${body.weatherList[0].id}, ${body.weatherList[0].main}, ${body.weatherList[0].description}, ${body.weatherList[0].icon}"
            }

        } else {
            event.message = "Error: " + response.code().toString() + "\n" + response.message()
        }

        EventBus.getDefault().post(event)
    }

    fun getCurrentWeather() = currentWeather
}