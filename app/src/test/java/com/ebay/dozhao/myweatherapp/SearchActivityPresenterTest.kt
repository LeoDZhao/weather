package com.ebay.dozhao.myweatherapp

import android.content.pm.PackageManager
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
    private lateinit var mockProgressBar: ProgressBar
    @Mock
    private lateinit var mockRecentSearchLayout: View
    @Mock
    private lateinit var mockPermissionChecker: PermissionChecker
    @Mock
    private lateinit var mockGeoLocation: GeoLocation
    @Mock
    private lateinit var mockNavigationUtils: NavigationUtils
    @Mock
    private lateinit var mockRecentSearchRepository: RecentSearchRepository

    private lateinit var searchActivityPresenter: SearchActivityPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        searchActivityPresenter = SearchActivityPresenter(mockActivity)
        searchActivityPresenter.setPermissionCheckerObject(mockPermissionChecker)
        searchActivityPresenter.setGeoLocationObject(mockGeoLocation)
        searchActivityPresenter.setNavigationUtils(mockNavigationUtils)
        searchActivityPresenter.setRecentSearchRepository(mockRecentSearchRepository)
        `when`(mockGpsLocationIcon.id).thenReturn(R.id.gps_location_icon)
        `when`(mockActivity.findViewById<View>(R.id.progressBar)).thenReturn(mockProgressBar)
        `when`(mockActivity.findViewById<View>(R.id.recent_search_layout)).thenReturn(mockRecentSearchLayout)
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

    @Test
    fun shouldHideRecentSearchLayoutWhenThereIsNoRecentSearch() {
        givenThereIsNoRecentSearch()
        whenDynamicallyChangeVisibilityForRecentSearchLayout()
        thenRecentSearchLayoutIsGone()
    }

    @Test
    fun shouldShowRecentSearchLayoutWhenThereIsOneRecentSearch() {
        givenThereIsOneRecentSearch()
        whenDynamicallyChangeVisibilityForRecentSearchLayout()
        thenRecentSearchLayoutIsVisible()
    }

    @Test
    fun shouldRequestLocationAndShowProgressBarWhenLocationPermissionIsGrantedByUser() {
        whenLocationPermissionIsGrantedByUser()
        thenSingleLocationUpdateIsRequested()
        thenProgressBarIsVisible()
    }

    private fun givenLocationPermissionIsGranted(boolean: Boolean) {
        `when`(mockPermissionChecker.isPermissionGranted(PermissionChecker.PermissionType.LOCATION)).thenReturn(boolean)
    }

    private fun givenThereIsNoRecentSearch() {
        `when`(mockRecentSearchRepository.recentSearches).thenReturn(ArrayList())
    }

    private fun givenThereIsOneRecentSearch() {
        val recentSearches = ArrayList<String>()
        recentSearches.add("test recent search")
        `when`(mockRecentSearchRepository.recentSearches).thenReturn(recentSearches)
    }

    private fun whenGpsLocationIconIsClicked() {
        searchActivityPresenter.onClick(mockGpsLocationIcon)
    }

    private fun whenLocaitonUpdateIsDone() {
        searchActivityPresenter.onLocationChangedEvent(LocationChangedEvent())
    }

    private fun whenDynamicallyChangeVisibilityForRecentSearchLayout() {
        searchActivityPresenter.dynamicallyChangeVisibilityForRecentSearchLayout()
    }

    private fun whenLocationPermissionIsGrantedByUser() {
        val permissions = arrayOf("")
        val grantResults = IntArray(1)
        grantResults[0] = PackageManager.PERMISSION_GRANTED
        searchActivityPresenter.onRequestPermissionsResult(PermissionChecker.PermissionType.LOCATION.ordinal, permissions, grantResults)
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

    private fun thenRecentSearchLayoutIsGone() {
        verify(mockRecentSearchLayout).visibility = View.GONE
    }

    private fun thenRecentSearchLayoutIsVisible() {
        verify(mockRecentSearchLayout).visibility = View.VISIBLE
    }
}