package com.ebay.dozhao.myweatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import com.ebay.dozhao.myweatherapp.event.LocationChangedEvent
import org.greenrobot.eventbus.EventBus

object GeoLocation : LocationListener {
    private const val INIT_IMPOSSIBLE_LAT = 91.0
    private const val INIT_IMPOSSIBLE_LON = 181.0
    var latitude = INIT_IMPOSSIBLE_LAT
    var longitude = INIT_IMPOSSIBLE_LON

    override fun onLocationChanged(location: Location?) {
        Log.d("dozhao", "onLocationChanged")
        location?.let {
            latitude = it.latitude
            longitude = it.longitude
            EventBus.getDefault().post(LocationChangedEvent())
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Log.d("dozhao", "onStatusChanged")
    }

    override fun onProviderEnabled(provider: String?) {
        Log.d("dozhao", "onProviderEnabled")
    }

    override fun onProviderDisabled(provider: String?) {
        Log.d("dozhao", "onProviderDisabled")
    }

    private var locationManager: LocationManager = MyApplication.instance?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @SuppressLint("MissingPermission")
    fun requestSingleLocationUpdate() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null)
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null)
        }
    }

    //For Test
    fun setLocationManager(locationManager: LocationManager) {
        this.locationManager = locationManager
    }
}

