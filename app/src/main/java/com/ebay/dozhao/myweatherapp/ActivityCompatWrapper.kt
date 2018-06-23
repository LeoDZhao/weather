package com.ebay.dozhao.myweatherapp

import android.app.Activity
import android.content.Context
import android.support.v4.app.ActivityCompat

object ActivityCompatWrapper {
    fun checkSelfPermission(context: Context, s: String): Int {
        return ActivityCompat.checkSelfPermission(context, s)
    }

    fun requestPermissions(activity: Activity, strings: Array<String>, ordinal: Int) {
        ActivityCompat.requestPermissions(activity, strings, ordinal)
    }
}