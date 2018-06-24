package com.ebay.dozhao.myweatherapp

import android.view.View
import android.widget.ProgressBar
import com.ebay.dozhao.myweatherapp.event.LocationChangedEvent
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

const val EXPECTED_LAT = 90.0
const val EXPECTED_LON = 180.0

@RunWith(RobolectricTestRunner::class)
class SearchActivityPresenterTest {

    @Mock
    private lateinit var mockActivity: SearchActivity
    @Mock
    private lateinit var mockGpsLocationIcon: View
    @Mock
    private lateinit var mockPermissionChecker: PermissionChecker
    @Mock
    private lateinit var mockGeoLocation: GeoLocation
    @Mock
    private lateinit var mockProgressBar: ProgressBar
    @Mock
    private lateinit var mockNavigationUtils: NavigationUtils

    private lateinit var searchActivityPresenter: SearchActivityPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        searchActivityPresenter = SearchActivityPresenter(mockActivity)
        searchActivityPresenter.setPermissionCheckerObject(mockPermissionChecker)
        searchActivityPresenter.setGeoLocationObject(mockGeoLocation)
        searchActivityPresenter.setNavigationUtils(mockNavigationUtils)
        `when`(mockGpsLocationIcon.id).thenReturn(R.id.gps_locaiton_icon)
        `when`(mockActivity.findViewById<View>(R.id.progressBar)).thenReturn(mockProgressBar)
        `when`(mockGeoLocation.latitude).thenReturn(EXPECTED_LAT)
        `when`(mockGeoLocation.longitude).thenReturn(EXPECTED_LON)
    }

    @Test
    fun shouldRequestLocationPermissionWhenGpsLocationIconIsClickedAndPermissionIsNotGranted() {
        givenLocationPermissionIsGranted(false)
        whenGpsLocationIconIsClicked()
        thenLocationPermissionShouldBeRequested()
    }

    @Test
    fun shouldRequestLocationAndShowProgressBarWhenGpsLocationIconIsClickedAndPermissionIsGranted() {
        givenLocationPermissionIsGranted(true)
        whenGpsLocationIconIsClicked()
        thenSingleLocationUpdateIsRequested()
        thenProgressBarIsVisible()
    }

    @Test
    fun shouldHideProgressBarAndStartSearchResultActivityWithCorrectLatLonWhenLocationUpdateIsDone() {
        whenLocaitonUpdateIsDone()
        thenProgressBarIsGone()
        thenStartSearchResultActivityWithQuery("lat=$EXPECTED_LAT&lon=$EXPECTED_LON")
    }

    private fun givenLocationPermissionIsGranted(boolean: Boolean) {
        `when`(mockPermissionChecker.isPermissionGranted(PermissionChecker.PermissionType.LOCATION)).thenReturn(boolean)
    }

    private fun whenGpsLocationIconIsClicked() {
        searchActivityPresenter.onClick(mockGpsLocationIcon)
    }

    private fun whenLocaitonUpdateIsDone() {
        searchActivityPresenter.onLocationChangedEvent(LocationChangedEvent())
    }

    private fun thenLocationPermissionShouldBeRequested() {
        verify(mockPermissionChecker).requestPermission(mockActivity, PermissionChecker.PermissionType.LOCATION)
    }

    private fun thenSingleLocationUpdateIsRequested() {
        verify(mockGeoLocation).requestSingleLocationUpdate()
    }

    private fun thenProgressBarIsVisible() {
        verify(mockProgressBar).visibility = View.VISIBLE
    }

    private fun thenProgressBarIsGone() {
        verify(mockProgressBar).visibility = View.GONE
    }

    private fun thenStartSearchResultActivityWithQuery(query: String) {
        verify(mockNavigationUtils).startSearchResultActivity(mockActivity, query)
    }
}