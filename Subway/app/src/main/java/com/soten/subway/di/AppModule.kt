package com.soten.subway.di

import android.app.Activity
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.soten.subway.BuildConfig
import com.soten.subway.data.api.StationApi
import com.soten.subway.data.api.StationArrivalsApi
import com.soten.subway.data.api.StationStorageApi
import com.soten.subway.data.api.Url
import com.soten.subway.data.db.AppDatabase
import com.soten.subway.data.preference.PreferenceManager
import com.soten.subway.data.preference.SharedPreferenceManager
import com.soten.subway.presenter.stationarrivals.StationArrivalsContract
import com.soten.subway.presenter.stationarrivals.StationArrivalsFragment
import com.soten.subway.presenter.stationarrivals.StationArrivalsPresenter
import com.soten.subway.presenter.stations.StationContract
import com.soten.subway.presenter.stations.StationsFragment
import com.soten.subway.presenter.stations.StationsPresenter
import com.soten.subway.repository.StationRepository
import com.soten.subway.repository.StationRepositoryImpl
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

val appModule = module {

    single { Dispatchers.IO }

    // Database
    single { AppDatabase.build(androidApplication()) }
    single { get<AppDatabase>().stationDao() }

    // Preference
    single { androidContext().getSharedPreferences("preference", Activity.MODE_PRIVATE) }
    single<PreferenceManager> { SharedPreferenceManager(get()) }

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
    single<StationArrivalsApi> {
        Retrofit.Builder().baseUrl(Url.SEOUL_DATA_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
            .create()
    }
    single<StationApi> { StationStorageApi(Firebase.storage) }

    // Repository
    single<StationRepository> { StationRepositoryImpl(get(), get(), get(), get(), get()) }

    // Presentation
    scope<StationsFragment> {
        scoped<StationContract.Presenter> { StationsPresenter(getSource(), get()) }
    } // StationsFragment 와 StationContract.Presenter 를 같은 스코프에 둬서 결속력이 강하게 만든다.
    scope<StationArrivalsFragment> {
        scoped<StationArrivalsContract.Presenter> { StationArrivalsPresenter(getSource(), get(), get()) }
    } // StationsFragment 와 StationContract.Presenter 를 같은 스코프에 둬서 결속력이 강하게 만든다.
}

/*
* single - 글로벌 스코프 (생명주기가 앱전체 해당
* factory - 서로 공유하거나 재사용 불가능
* scoped - scope 내에서 재사용 가능
* */