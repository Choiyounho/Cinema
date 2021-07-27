package com.soten.deliverycheck.di

import com.soten.deliverycheck.BuildConfig
import com.soten.deliverycheck.api.SweetTrackerApi
import com.soten.deliverycheck.api.Url
import com.soten.deliverycheck.db.AppDatabase
import com.soten.deliverycheck.repository.TrackingItemRepository
import com.soten.deliverycheck.repository.TrackingRepositoryImpl
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

val appModule = module {

    single { Dispatchers.IO }

    // Database
    single { AppDatabase.build(androidApplication()) }
    single { get<AppDatabase>().trackingItemDao() }

    // Api
    single {
        OkHttpClient()
            .newBuilder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            ).build()
    }

    single<SweetTrackerApi> {
        Retrofit.Builder().baseUrl(Url.SWEET_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
            .create()
    }

    // Repository
    single <TrackingItemRepository> { TrackingRepositoryImpl(get(), get(), get()) }

}