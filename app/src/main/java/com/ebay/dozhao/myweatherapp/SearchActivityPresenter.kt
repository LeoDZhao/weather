package com.ebay.dozhao.myweatherapp

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import android.widget.SearchView
import com.ebay.dozhao.myweatherapp.event.LocationChangedEvent
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class SearchActivityPresenter(private val activity: SearchActivity) : View.OnClickListener {
    @SuppressLint("MissingPermission")
    override fun onClick(view: View?) {
        if (view?.id == R.id.gps_locaiton_icon) {
            if (!GeoLocation.isLocationPermissionGranted()) {
                GeoLocation.requestLocationPermission(activity)
            } else {
                requestLocationUpdates()
            }
        }
    }

    fun cleanSearchView() {
        val searchView: SearchView = activity.findViewById(R.id.search_view)
        val searchManager: SearchManager = activity.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val componentName = ComponentName(activity, SearchResultActivity::class.java)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setQuery("", false)
        searchView.queryHint = activity.getString(R.string.search_hint)
        searchView.setIconifiedByDefault(false)
        searchView.clearFocus()
    }

    fun setVisibilityForRecentSearch() {
        val recentSearchLayout = activity.findViewById<View>(R.id.recent_search_layout)
        if (RecentSearchRepository.recentSearches.isEmpty()) {
            recentSearchLayout.visibility = View.GONE
        } else {
            recentSearchLayout.visibility = View.VISIBLE
        }
    }

    private fun requestLocationUpdates() {
        GeoLocation.requestLocationUpdates()
        val progressBar = activity.findViewById<View>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
    }

    private fun goToSearchResultActivityWithLatLon() {
        val latitude = GeoLocation.latitude
        val longitude = GeoLocation.longitude
        val query = "lat=$latitude&lon=$longitude"
        NavigationUtils.startSearchResultActivity(activity, query)
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PermissionChecker.PermissionType.LOCATION.ordinal -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    requestLocationUpdates()
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

        // Add other 'when' lines to check for other
        // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLocationChangedEvent(event: LocationChangedEvent) {
        val progressBar = activity.findViewById<View>(R.id.progressBar)
        progressBar.visibility = View.GONE
        goToSearchResultActivityWithLatLon()
    }

}