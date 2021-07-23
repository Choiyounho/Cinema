package com.soten.subway.di

import android.app.Activity
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.soten.subway.data.api.StationApi
import com.soten.subway.data.api.StationStorageApi
import com.soten.subway.data.db.AppDatabase
import com.soten.subway.data.preference.PreferenceManager
import com.soten.subway.data.preference.SharedPreferenceManager
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

}