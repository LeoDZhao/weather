package com.ebay.dozhao.myweatherapp

import com.ebay.dozhao.myweatherapp.event.SearchDoneEvent
import com.ebay.dozhao.myweatherapp.raw.RawCurrentWeather
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import retrofit2.Call
import retrofit2.Response

const val TEST_RESPONSE_CODE = 404
const val TEST_RESPONSE_MESSAGE = "Not Found"
const val TEST_LOCATION_NAME_QUERY = "shanghai"

@RunWith(RobolectricTestRunner::class)
class WeatherRepositoryTest {
    @Mock
    private lateinit var mockWebService: WeatherService
    @Mock
    private lateinit var mockCall: Call<RawCurrentWeather>
    @Mock
    private lateinit var mockRecentSearchRepository: RecentSearchRepository
    @Mock
    private lateinit var mockResponse: Response<RawCurrentWeather>
    @Mock
    private lateinit var mockRawCurrentWeather: RawCurrentWeather
    @Mock
    private lateinit var mockSearchDoneEvent: SearchDoneEvent

    private var weatherRepository = WeatherRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        weatherRepository.setWeatherService(mockWebService)
        weatherRepository.setRecentSearchRepository(mockRecentSearchRepository)
        weatherRepository.setSearchDoneEvent(mockSearchDoneEvent)
        `when`(mockWebService.currentWeatherByLatLon(anyString(), anyString())).thenReturn(mockCall)
        `when`(mockWebService.currentWeatherByZipCode(anyString())).thenReturn(mockCall)
        `when`(mockWebService.currentWeatherByLocationName(anyString())).thenReturn(mockCall)
    }

    @Test
    fun shouldGetCurrentWeatherByCorrectLatLonWhenQueryIsGeoLocationWithPositiveLatAndPositiveLon() {
        val lat = "45.0"
        val lon = "90.0"
        whenRequestWeatherFromAPI("lat=$lat&lon=$lon")
        thenGetCurrentWeatherByLatLon(lat, lon)
    }

    @Test
    fun shouldGetCurrentWeatherByCorrectLatLonWhenQueryIsGeoLocationWithPositiveLatAndNegativeLon() {
        val lat = "45.0"
        val lon = "-90.0"
        whenRequestWeatherFromAPI("lat=$lat&lon=$lon")
        thenGetCurrentWeatherByLatLon(lat, lon)
    }

    @Test
    fun shouldGetCurrentWeatherByCorrectLatLonWhenQueryIsGeoLocationWithNegativeLatAndPositiveLon() {
        val lat = "-45.0"
        val lon = "90.0"
        whenRequestWeatherFromAPI("lat=$lat&lon=$lon")
        thenGetCurrentWeatherByLatLon(lat, lon)
    }

    @Test
    fun shouldGetCurrentWeatherByCorrectLatLonWhenQueryIsGeoLocationWithNegativeLatAndNegativeLon() {
        val lat = "-45.0"
        val lon = "-90.0"
        whenRequestWeatherFromAPI("lat=$lat&lon=$lon")
        thenGetCurrentWeatherByLatLon(lat, lon)
    }

    @Test
    fun shouldGetCurrentWeatherByCorrectZipCodeWhenQueryIsValidZipCode() {
        val query = "94040"
        whenRequestWeatherFromAPI(query)
        thenGetCurrentWeatherByZipCode(query)
    }

    @Test
    fun shouldGetCurrentWeatherByLocationNameWhenQueryIsNotGeoLocationOrZipCode() {
        whenRequestWeatherFromAPI(TEST_LOCATION_NAME_QUERY)
        thenGetCurrentWeatherByLocaitonName(TEST_LOCATION_NAME_QUERY)
    }

    @Test
    fun shouldAddQueryToRecentSearchWhenResponseIsSuccessfulAndHasBody() {
        givenResponseIsSuccessfulAndHasBody()
        whenRequestWeatherFromAPI(TEST_LOCATION_NAME_QUERY)
        thenQueryIsAddedToRecentSearch(TEST_LOCATION_NAME_QUERY)
    }

    @Test
    fun shouldHasCorrectErrorMessageInSearchDoneEventWhenResponseIsNull() {
        givenResponseIsNull()
        whenRequestWeatherFromAPI(TEST_LOCATION_NAME_QUERY)
        thenErrorMessageInSearchDoneEventIs(SearchDoneEvent.ErrorMessageDetail.NO_RESPONSE.toString())
    }

    @Test
    fun shouldHasCorrectErrorMessageInSearchDoneEventWhenResponseIsSuccessfulButResponseBodyIsNull() {
        givenResponseIsSuccessfulButHasNoBody()
        whenRequestWeatherFromAPI(TEST_LOCATION_NAME_QUERY)
        thenErrorMessageInSearchDoneEventIs(SearchDoneEvent.ErrorMessageDetail.NO_RESPONSE_BODY.toString())
    }

    @Test
    fun shouldHasCorrectErrorMessageInSearchDoneEventWhenResponseIsNotSuccessful() {
        givenResponseIsNotSuccessful()
        givenResponseHasStatusCodeAndMessage(TEST_RESPONSE_CODE, TEST_RESPONSE_MESSAGE)
        whenRequestWeatherFromAPI(TEST_LOCATION_NAME_QUERY)
        val expectedMessage = SearchDoneEvent.ErrorMessageDetail.RESPONSE_NOT_SUCCESSFUL.toString() + "\n" +
                TEST_RESPONSE_CODE.toString() + "\n" +
                TEST_RESPONSE_MESSAGE
        thenErrorMessageInSearchDoneEventIs(expectedMessage)
    }

    @Test
    fun shouldHasEmptyErrorMessageInSearchDoneEventWhenResponseIsSuccessfulAndHasBody() {
        givenResponseIsSuccessfulAndHasBody()
        whenRequestWeatherFromAPI(TEST_LOCATION_NAME_QUERY)
        thenErrorMessageInSearchDoneEventIs("")
    }

    private fun givenResponseHasStatusCodeAndMessage(code: Int, message: String) {
        `when`(mockResponse.code()).thenReturn(code)
        `when`(mockResponse.message()).thenReturn(message)
    }

    private fun givenResponseIsNull() {
        `when`(mockCall.execute()).thenReturn(null)
    }

    private fun givenResponseIsNotSuccessful() {
        `when`(mockCall.execute()).thenReturn(mockResponse)
        `when`(mockResponse.isSuccessful).thenReturn(false)
    }

    private fun givenResponseIsSuccessfulButHasNoBody() {
        `when`(mockCall.execute()).thenReturn(mockResponse)
        `when`(mockResponse.isSuccessful).thenReturn(true)
        `when`(mockResponse.body()).thenReturn(null)
    }

    private fun givenResponseIsSuccessfulAndHasBody() {
        `when`(mockCall.execute()).thenReturn(mockResponse)
        `when`(mockResponse.isSuccessful).thenReturn(true)
        `when`(mockResponse.body()).thenReturn(mockRawCurrentWeather)
    }

    private fun whenRequestWeatherFromAPI(query: String) {
        weatherRepository.requestWeatherFromAPI(query)
    }

    private fun thenGetCurrentWeatherByLatLon(lat: String, lon: String) {
        verify(mockWebService).currentWeatherByLatLon(lat, lon)
    }

    private fun thenGetCurrentWeatherByZipCode(zipCode: String) {
        verify(mockWebService).currentWeatherByZipCode(zipCode)
    }

    private fun thenGetCurrentWeatherByLocaitonName(locationName: String) {
        verify(mockWebService).currentWeatherByLocationName(locationName)
    }

    private fun thenQueryIsAddedToRecentSearch(query: String) {
        verify(mockRecentSearchRepository).addRecentSearch(query)
    }

    private fun thenErrorMessageInSearchDoneEventIs(errorMessage: String) {
        verify(mockSearchDoneEvent).errorMessage = errorMessage
    }
}