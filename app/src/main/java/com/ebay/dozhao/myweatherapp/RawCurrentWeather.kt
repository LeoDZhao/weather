package com.ebay.dozhao.myweatherapp

import com.google.gson.annotations.SerializedName

class RawCurrentWeather {
    var id: Int = 0
    var name: String = "unknown"
    var cod: Int = 0
    var coord: RawCoord = RawCoord()

    @SerializedName("weather")
    var weatherList: List<RawWeather> = ArrayList()
}