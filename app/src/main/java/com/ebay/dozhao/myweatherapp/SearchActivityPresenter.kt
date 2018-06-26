package com.ebay.dozhao.myweatherapp

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import com.ebay.dozhao.myweatherapp.event.LocationChangedEvent
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class SearchActivityPresenter(private val activity: SearchActivity) : View.OnClickListener {
    private var permissionChecker: PermissionChecker = PermissionChecker
    private var geoLocation: GeoLocation = GeoLocation
    private var navigationUtils: NavigationUtils = NavigationUtils
    private var recentSearchRepository: RecentSearchRepository = RecentSearchRepository

    @SuppressLint("MissingPermission")
    override fun onClick(view: View?) {
        if (view?.id == R.id.gps_location_icon) {
            if (!permissionChecker.isPermissionGranted(PermissionChecker.PermissionType.LOCATION)) {
                permissionChecker.requestPermission(activity, PermissionChecker.PermissionType.LOCATION)
            } else {
                requestSingleLocationUpdate()
                showProgressBar()
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

    fun configureRecentSearchRecyclerView() {
        val viewManager = LinearLayoutManager(activity)
        val viewAdapter = RecentSearchRecyclerViewAdapter(recentSearchRepository.recentSearches)
        val recentSearchRecyclerView = activity.findViewById<RecyclerView>(R.id.recent_search_recycler_view)
        recentSearchRecyclerView.setHasFixedSize(true)
        recentSearchRecyclerView.layoutManager = viewManager
        recentSearchRecyclerView.adapter = viewAdapter
    }

    fun dynamicallyChangeVisibilityForRecentSearchLayout() {
        val recentSearchLayout = activity.findViewById<View>(R.id.recent_search_layout)
        if (recentSearchRepository.recentSearches.isEmpty()) {
            recentSearchLayout.visibility = View.GONE
        } else {
            recentSearchLayout.visibility = View.VISIBLE
        }
    }

    private fun requestSingleLocationUpdate() {
        geoLocation.requestSingleLocationUpdate()
    }

    private fun showProgressBar() {
        val progressBar = activity.findViewById<View>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PermissionChecker.PermissionType.LOCATION.ordinal -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    requestSingleLocationUpdate()
                    showProgressBar()
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
        if (event.locationUpdated) {
            val latitude = geoLocation.latitude
            val longitude = geoLocation.longitude
            val query = "lat=$latitude&lon=$longitude"
            navigationUtils.startSearchResultActivity(activity, query)
        } else {
            Toast.makeText(activity, "Get GPS location timeout.", Toast.LENGTH_SHORT).show()
        }
    }

    //For test
    fun setPermissionCheckerObject(permissionChecker: PermissionChecker) {
        this.permissionChecker = permissionChecker
    }

    //For test
    fun setGeoLocationObject(geoLocation: GeoLocation) {
        this.geoLocation = geoLocation
    }

    //For test
    fun setNavigationUtils(navigationUtils: NavigationUtils) {
        this.navigationUtils = navigationUtils
    }

    //For test
    fun setRecentSearchRepository(recentSearchRepository: RecentSearchRepository) {
        this.recentSearchRepository = recentSearchRepository
    }

}