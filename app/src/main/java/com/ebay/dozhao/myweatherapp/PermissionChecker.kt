package com.ebay.dozhao.myweatherapp

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build

object PermissionChecker {
    enum class PermissionType {
        LOCATION;

        override fun toString(): String {
            return when (this) {
                LOCATION -> Manifest.permission.ACCESS_FINE_LOCATION
            }
        }
    }

    fun isPermissionGranted(type: PermissionType): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                ActivityCompatWrapper.checkSelfPermission(MyApplication.instance!!, type.toString()) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(activity: Activity, type: PermissionType) {
        if (isPermissionGranted(type)) {
            return
        } else {
            ActivityCompatWrapper.requestPermissions(activity, arrayOf(type.toString()), type.ordinal)
        }
    }
}