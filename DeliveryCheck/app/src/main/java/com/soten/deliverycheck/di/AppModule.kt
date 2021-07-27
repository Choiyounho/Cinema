package com.soten.deliverycheck.di

import com.soten.deliverycheck.BuildConfig
import com.soten.deliverycheck.api.SweetTrackerApi
import com.soten.deliverycheck.api.Url
import com.soten.deliverycheck.db.AppDatabase
import com.soten.deliverycheck.presenter.trackingitems.TrackingItemsContract
import com.soten.deliverycheck.presenter.trackingitems.TrackingItemsFragment
import com.soten.deliverycheck.presenter.trackingitems.TrackingItemsPresenter
import com.soten.deliverycheck.repository.TrackingItemRepository
import com.soten.deliverycheck.repository.TrackingItemRepositoryStub
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
//    single <TrackingItemRepository> { TrackingRepositoryImpl(get(), get(), get()) }
    // Stub 을 전달한다. Stub 에서도 인터페이스가 동일하게 구현돼 있어 프레젠터에서 차이를 못 느끼고 호출을 한다.
    // 실제 데이터가 없더라도 화면을 미리 구성할 수 있다
    single <TrackingItemRepository> { TrackingItemRepositoryStub() }

    // Presenter
    scope<TrackingItemsFragment> {
        scoped<TrackingItemsContract.Presenter> { TrackingItemsPresenter(getSource(), get()) }
    } // getSource()를 통해 Presenter 그 자체로 전달
}