package com.soten.fooddelivery.di

import com.soten.fooddelivery.screen.main.home.HomeViewModel
import com.soten.fooddelivery.screen.main.my.MyViewModel
import com.soten.fooddelivery.util.provider.ResourceProvider
import com.soten.fooddelivery.util.provider.ResourceProviderImpl
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { HomeViewModel() }
    viewModel { MyViewModel() }

    single { provideGsonConvertFactory() }
    single { buildOkHttpClient() }

    single { provideRetrofit(get(), get()) }

    single<ResourceProvider> { ResourceProviderImpl(androidApplication()) }

    // Coroutine
    single { Dispatchers.IO }
    single { Dispatchers.Main }

}

/*
* 레퍼지토리패턴을 사용 시
* 1. 보일러플레이트 코드
* 2.
* */