package com.soten.moviereview

import android.app.Application
import com.soten.moviereview.di.appModule
import com.soten.moviereview.utility.MovieDataGenerator
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MovieReviewApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {

            androidContext(this@MovieReviewApplication)
            modules(appModule)
        }

//        MovieDataGenerator().generate()
    }

}