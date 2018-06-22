package com.ebay.dozhao.myweatherapp

import com.ebay.dozhao.myweatherapp.raw.RawCurrentWeather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

private const val APIKEY = "95d190a434083879a6398aafd54d9e73"

interface WeatherService {
    @GET("data/2.5/weather?APPID=$APIKEY")
    fun currentWeatherByCityName(@Query("q") q: String): Call<RawCurrentWeather>

    @GET("data/2.5/weather?APPID=$APIKEY")
    fun currentWeatherByZipCode(@Query("zip") zip: String): Call<RawCurrentWeather>
}