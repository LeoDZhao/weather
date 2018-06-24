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
            response = weatherService.currentWeatherByLocationName(query).execute()
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