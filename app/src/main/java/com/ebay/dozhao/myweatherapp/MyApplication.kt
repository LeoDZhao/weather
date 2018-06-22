package com.ebay.dozhao.myweatherapp

import android.app.Application

class MyApplication : Application() {
    companion object {
        var instance: MyApplication? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}