package com.soten.fooddelivery.di

import com.soten.fooddelivery.data.repository.RestaurantRepository
import com.soten.fooddelivery.data.repository.RestaurantRepositoryImpl
import com.soten.fooddelivery.screen.main.home.HomeViewModel
import com.soten.fooddelivery.screen.main.home.restaurant.RestaurantCategory
import com.soten.fooddelivery.screen.main.home.restaurant.RestaurantListViewModel
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
    viewModel { (restaurantCategory: RestaurantCategory) -> RestaurantListViewModel(restaurantCategory, get()) }

    single<RestaurantRepository> { RestaurantRepositoryImpl(get(), get()) }

    single { provideGsonConvertFactory() }
    single { buildOkHttpClient() }

    single { provideRetrofit(get(), get()) }

    single<ResourceProvider> { ResourceProviderImpl(androidApplication()) }

    // Coroutine
    single { Dispatchers.IO }
    single { Dispatchers.Main }

}