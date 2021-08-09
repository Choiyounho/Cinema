package com.soten.fooddelivery.di

import com.soten.fooddelivery.data.entity.LocationLatLngEntity
import com.soten.fooddelivery.data.entity.MapSearchInformationEntity
import com.soten.fooddelivery.data.entity.RestaurantEntity
import com.soten.fooddelivery.data.repository.map.MapRepository
import com.soten.fooddelivery.data.repository.map.MapRepositoryImpl
import com.soten.fooddelivery.data.repository.restaurant.RestaurantRepository
import com.soten.fooddelivery.data.repository.restaurant.RestaurantRepositoryImpl
import com.soten.fooddelivery.data.repository.user.UserRepository
import com.soten.fooddelivery.data.repository.user.UserRepositoryImpl
import com.soten.fooddelivery.screen.main.home.HomeViewModel
import com.soten.fooddelivery.screen.main.home.restaurant.RestaurantCategory
import com.soten.fooddelivery.screen.main.home.restaurant.RestaurantListViewModel
import com.soten.fooddelivery.screen.main.home.restaurant.detail.RestaurantDetailViewModel
import com.soten.fooddelivery.screen.main.my.MyViewModel
import com.soten.fooddelivery.screen.mylocation.MyLocationViewModel
import com.soten.fooddelivery.util.provider.ResourceProvider
import com.soten.fooddelivery.util.provider.ResourceProviderImpl
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { HomeViewModel(get(), get()) }
    viewModel { MyViewModel() }
    viewModel { (restaurantCategory: RestaurantCategory,
                    locationLatLngEntity: LocationLatLngEntity) ->
        RestaurantListViewModel(
            restaurantCategory,
            locationLatLngEntity,
            get()
        )
    }
    viewModel { (mapSearchInformationEntity: MapSearchInformationEntity) ->
        MyLocationViewModel(mapSearchInformationEntity, get(), get())
    }
    viewModel { (restaurantEntity: RestaurantEntity) -> RestaurantDetailViewModel(restaurantEntity) }

    single<RestaurantRepository> { RestaurantRepositoryImpl(get(), get(), get()) }
    single<MapRepository> { MapRepositoryImpl(get(), get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }

    single { provideGsonConvertFactory() }
    single { buildOkHttpClient() }

    single { provideMapRetrofit(get(), get()) }
    single { provideMapAipService(get()) }

    single { provideDb(androidApplication()) }
    single { provideLocationDao(get()) }

    single<ResourceProvider> { ResourceProviderImpl(androidApplication()) }

    // Coroutine
    single { Dispatchers.IO }
    single { Dispatchers.Main }

}