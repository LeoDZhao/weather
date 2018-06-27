package com.ebay.dozhao.myweatherapp

import android.app.SearchManager
import android.content.Intent
import android.view.View
import android.widget.TextView
import com.ebay.dozhao.myweatherapp.event.SearchDoneEvent
import com.ebay.dozhao.myweatherapp.raw.RawCurrentWeather
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SearchResultActivityPresenterTest {
    @Mock
    private lateinit var mockProgressBar: View
    @Mock
    private lateinit var mockErrorMessage: TextView
    @Mock
    private lateinit var mockWeatherDetailLayout: View
    @Mock
    private lateinit var mockCurrentWeather: RawCurrentWeather
    @Mock
    private lateinit var mockActivity: SearchResultActivity

    private lateinit var searchResultActivityPresenter: SearchResultActivityPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        `when`(mockActivity.findViewById<View>(R.id.progressBar)).thenReturn(mockProgressBar)
        `when`(mockActivity.findViewById<View>(R.id.weather_detail_layout)).thenReturn(mockWeatherDetailLayout)
        `when`(mockActivity.findViewById<TextView>(R.id.error_message)).thenReturn(mockErrorMessage)
        searchResultActivityPresenter = SearchResultActivityPresenter(mockActivity)
    }

    @Test
    fun shouldHideDetailLayoutAndShowProgressBar() {
        whenProcessSearchIntent()
        thenDetailLayoutIsHidden()
        thenProgressBarIsShown()
    }

    @Test
    fun shouldHideProgressBarAndShowExpectedErrorMessageWhenSearchIsDoneWithError() {
        val error = SearchDoneEvent.ErrorMessageDetail.NO_RESPONSE.toString()
        whenSearchIsDoneWithError(error)
        thenProgressBarIsHidden()
        thenErrorMessageIsShown(error)
    }

    @Test
    fun shouldNotShowDetailLayoutWhenSearchIsDoneWithError() {
        val error = SearchDoneEvent.ErrorMessageDetail.NO_RESPONSE.toString()
        whenSearchIsDoneWithError(error)
        thenNotShowDetailLayout()
    }

    @Test
    fun shouldHideProgressBarAndShowDetailLayoutWhenSearchIsDoneWithoutError() {
        whenSearchIsDoneWithError("")
        thenProgressBarIsHidden()
        thenDetailLayoutIsShown()
    }

    private fun whenProcessSearchIntent() {
        val intent = Intent()
        intent.action = Intent.ACTION_SEARCH
        intent.putExtra(SearchManager.QUERY, "")
        searchResultActivityPresenter.processIntent(intent)
    }

    private fun whenSearchIsDoneWithError(error: String) {
        val event = SearchDoneEvent()
        event.errorMessage = error
        searchResultActivityPresenter.onSearchDoneEvent(event)
    }

    private fun thenProgressBarIsHidden() {
        verify(mockProgressBar).visibility = View.GONE
    }

    private fun thenProgressBarIsShown() {
        verify(mockProgressBar).visibility = View.VISIBLE
    }

    private fun thenErrorMessageIsShown(errorMessage: String) {
        verify(mockErrorMessage).text = errorMessage
        verify(mockErrorMessage).visibility = View.VISIBLE
    }

    private fun thenDetailLayoutIsHidden() {
        verify(mockWeatherDetailLayout).visibility = View.GONE
    }

    private fun thenDetailLayoutIsShown() {
        verify(mockWeatherDetailLayout).visibility = View.VISIBLE
    }

    private fun thenNotShowDetailLayout() {
        verify(mockWeatherDetailLayout, never()).visibility = View.VISIBLE
    }
}