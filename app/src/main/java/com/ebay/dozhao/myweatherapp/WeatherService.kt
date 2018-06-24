package com.ebay.dozhao.myweatherapp

import com.ebay.dozhao.myweatherapp.raw.RawCurrentWeather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = "95d190a434083879a6398aafd54d9e73"

interface WeatherService {
    @GET("data/2.5/weather?APPID=$API_KEY")
    fun currentWeatherByLocationName(@Query("q") locaitonName: String): Call<RawCurrentWeather>

    @GET("data/2.5/weather?APPID=$API_KEY")
    fun currentWeatherByZipCode(@Query("zip") zipCode: String): Call<RawCurrentWeather>

    @GET("data/2.5/weather?APPID=$API_KEY")
    fun currentWeatherByLatLon(@Query("lat") lat: String, @Query("lon") lon: String): Call<RawCurrentWeather>
}