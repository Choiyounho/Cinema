package com.soten.fooddelivery

import android.app.Application
import android.content.Context

class DeliveryApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

    override fun onTerminate() {
        super.onTerminate()
        appContext = null
    }

    companion object {
        var appContext: Context? = null
            private set
    }

}