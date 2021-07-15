package com.soten.todo

import android.app.Application
import com.soten.todo.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class TodoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // TODO Koin Trigger
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@TodoApplication)
            modules(appModule)
        }
    }
}