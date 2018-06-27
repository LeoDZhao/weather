package com.ebay.dozhao.myweatherapp

import com.ebay.dozhao.myweatherapp.event.SearchDoneEvent
import com.ebay.dozhao.myweatherapp.raw.RawCurrentWeather
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.regex.Pattern

object WeatherRepository {
    private var currentWeather: RawCurrentWeather? = null
    private var weatherService: WeatherService
    private var recentSearchRepository: RecentSearchRepository = RecentSearchRepository
    private var searchDoneEvent: SearchDoneEvent = SearchDoneEvent()

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        weatherService = retrofit.create(WeatherService::class.java)
    }

    fun requestWeatherFromAPI(query: String) {
        if (query.isEmpty()) return

        var call: Call<RawCurrentWeather>? = null

        val regexForDouble = "-?\\d+\\.?\\d*"
        if (query.matches("^lat=$regexForDouble&lon=$regexForDouble$".toRegex())) {
            val pattern = Pattern.compile(regexForDouble)
            val matcher = pattern.matcher(query)
            if (matcher.find()) {
                val lat = matcher.group(0)
                if (matcher.find()) {
                    val lon = matcher.group(0)
                    call = weatherService.currentWeatherByLatLon(lat, lon)
                }
            }
        } else if (query.matches("^\\d+(?:[-\\s]\\d+)?\$".toRegex())) {
            call = weatherService.currentWeatherByZipCode(query)
        } else {
            call = weatherService.currentWeatherByLocationName(query)
        }

        val response: Response<RawCurrentWeather>?
        try {
            response = call?.execute()
        } catch (ioException: IOException) {
            searchDoneEvent.errorMessage = SearchDoneEvent.ErrorMessageDetail.NETWORK_EXCEPTION.toString()
            EventBus.getDefault().post(searchDoneEvent)
            return
        }

        searchDoneEvent.errorMessage = ""
        when {
            response == null -> searchDoneEvent.errorMessage = SearchDoneEvent.ErrorMessageDetail.NO_RESPONSE.toString()
            response.isSuccessful -> {
                val body = response.body()
                if (body == null) {
                    searchDoneEvent.errorMessage = SearchDoneEvent.ErrorMessageDetail.NO_RESPONSE_BODY.toString()
                } else {
                    currentWeather = body
                    recentSearchRepository.addRecentSearch(query)
                }
            }
            else -> searchDoneEvent.errorMessage = SearchDoneEvent.ErrorMessageDetail.RESPONSE_NOT_SUCCESSFUL.toString() + "\n" +
                    response.code().toString() + "\n" +
                    response.message()
        }
        EventBus.getDefault().post(searchDoneEvent)
    }

    fun getCurrentWeather() = currentWeather

    //Fot test
    fun setWeatherService(weatherService: WeatherService) {
        this.weatherService = weatherService
    }

    //For test
    fun setRecentSearchRepository(recentSearchRepository: RecentSearchRepository) {
        this.recentSearchRepository = recentSearchRepository
    }

    //For test
    fun setSearchDoneEvent(searchDoneEvent: SearchDoneEvent) {
        this.searchDoneEvent = searchDoneEvent
    }
}