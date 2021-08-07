package com.soten.fooddelivery

import android.app.Application
import android.content.Context
import com.soten.fooddelivery.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DeliveryApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this

        startKoin {
            androidContext(this@DeliveryApplication)
            modules(appModule)
        }
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