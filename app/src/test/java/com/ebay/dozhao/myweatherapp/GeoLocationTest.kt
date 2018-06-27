package com.ebay.dozhao.myweatherapp

import android.location.LocationManager
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class GeoLocationTest {
    @Mock
    private
    lateinit var mockLocationManager: LocationManager

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun shouldRequestSingleUpdateByGpsProviderWhenGpsProviderIsEnable() {
        givenGpsProviderIsEnabled(true)
        whenRequestSingleLocationUpdate()
        thenGpsProviderIsUsedByeLocationManagerToRequestSingleUpdate()
    }

    @Test
    fun shouldRequestSingleUpdateByNetWorkProviderWhenNetWorkProviderIsEnable() {
        givenNetworkProviderIsEnabled(true)
        whenRequestSingleLocationUpdate()
        thenNetworkProviderIsUsedByeLocationManagerToRequestSingleUpdate()
    }

    @Test
    fun shouldNotRequestSingleLocationUpdateWhenBothGpsAndNetWorkProviderAreDisabled() {
        givenGpsProviderIsEnabled(false)
        givenNetworkProviderIsEnabled(false)
        whenRequestSingleLocationUpdate()
        thenLocationManagerDoesNotRequestSingleUpdate()
    }

    private fun givenGpsProviderIsEnabled(boolean: Boolean) {
        `when`(mockLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(boolean)
    }

    private fun givenNetworkProviderIsEnabled(boolean: Boolean) {
        `when`(mockLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)).thenReturn(boolean)
    }

    private fun whenRequestSingleLocationUpdate() {
        GeoLocation.requestSingleLocationUpdate(mockLocationManager)
    }

    private fun thenGpsProviderIsUsedByeLocationManagerToRequestSingleUpdate() {
        verify(mockLocationManager).requestSingleUpdate(eq(LocationManager.GPS_PROVIDER), any(), any())
    }

    private fun thenNetworkProviderIsUsedByeLocationManagerToRequestSingleUpdate() {
        verify(mockLocationManager).requestSingleUpdate(eq(LocationManager.NETWORK_PROVIDER), any(), any())
    }

    private fun thenLocationManagerDoesNotRequestSingleUpdate() {
        verify(mockLocationManager, never()).requestSingleUpdate(anyString(), any(), any())
    }
}