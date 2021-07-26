package com.soten.subway.di

import android.app.Activity
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.soten.subway.data.api.StationApi
import com.soten.subway.data.api.StationStorageApi
import com.soten.subway.data.db.AppDatabase
import com.soten.subway.data.preference.PreferenceManager
import com.soten.subway.data.preference.SharedPreferenceManager
import com.soten.subway.presenter.stations.StationContract
import com.soten.subway.presenter.stations.StationsFragment
import com.soten.subway.presenter.stations.StationsPresenter
import com.soten.subway.repository.StationRepository
import com.soten.subway.repository.StationRepositoryImpl
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    single { Dispatchers.IO }

    // Database
    single { AppDatabase.build(androidApplication()) }
    single { get<AppDatabase>().stationDao() }

    // Preference
    single { androidContext().getSharedPreferences("preference", Activity.MODE_PRIVATE) }
    single<PreferenceManager> { SharedPreferenceManager(get()) }

    // Api
    single<StationApi> { StationStorageApi(Firebase.storage) }

    // Repository
    single<StationRepository> { StationRepositoryImpl(get(), get(), get(), get()) }

    // Presentation
    scope<StationsFragment> {
        scoped<StationContract.Presenter> { StationsPresenter(getSource(), get()) }
    } // StationsFragment 와 StationContract.Presenter 를 같은 스코프에 둬서 결속력이 강하게 만든다.
}

/*
* single - 글로벌 스코프 (생명주기가 앱전체 해당
* factory - 서로 공유하거나 재사용 불가능
* scoped - scope 내에서 재사용 가능
* */