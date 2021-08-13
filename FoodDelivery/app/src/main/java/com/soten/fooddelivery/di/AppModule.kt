package com.soten.fooddelivery.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.soten.fooddelivery.data.entity.LocationLatLngEntity
import com.soten.fooddelivery.data.entity.MapSearchInformationEntity
import com.soten.fooddelivery.data.entity.RestaurantEntity
import com.soten.fooddelivery.data.entity.RestaurantFoodEntity
import com.soten.fooddelivery.data.preference.AppPreferenceManager
import com.soten.fooddelivery.data.repository.map.MapRepository
import com.soten.fooddelivery.data.repository.map.MapRepositoryImpl
import com.soten.fooddelivery.data.repository.order.OrderRepository
import com.soten.fooddelivery.data.repository.order.OrderRepositoryImpl
import com.soten.fooddelivery.data.repository.restaurant.RestaurantRepository
import com.soten.fooddelivery.data.repository.restaurant.RestaurantRepositoryImpl
import com.soten.fooddelivery.data.repository.restaurant.food.RestaurantFoodRepository
import com.soten.fooddelivery.data.repository.restaurant.food.RestaurantFoodRepositoryImpl
import com.soten.fooddelivery.data.repository.restaurant.review.RestaurantReviewRepository
import com.soten.fooddelivery.data.repository.restaurant.review.RestaurantReviewRepositoryImpl
import com.soten.fooddelivery.data.repository.user.UserRepository
import com.soten.fooddelivery.data.repository.user.UserRepositoryImpl
import com.soten.fooddelivery.screen.main.home.HomeViewModel
import com.soten.fooddelivery.screen.main.home.restaurant.RestaurantCategory
import com.soten.fooddelivery.screen.main.home.restaurant.RestaurantListViewModel
import com.soten.fooddelivery.screen.main.home.restaurant.detail.RestaurantDetailViewModel
import com.soten.fooddelivery.screen.main.home.restaurant.detail.menu.RestaurantMenuListViewModel
import com.soten.fooddelivery.screen.main.home.restaurant.detail.review.RestaurantReviewListViewModel
import com.soten.fooddelivery.screen.main.like.RestaurantLikeListViewModel
import com.soten.fooddelivery.screen.main.my.MyViewModel
import com.soten.fooddelivery.screen.mylocation.MyLocationViewModel
import com.soten.fooddelivery.screen.order.OrderMenuListViewModel
import com.soten.fooddelivery.screen.review.gallery.GalleryPhotoRepository
import com.soten.fooddelivery.screen.review.gallery.GalleryViewModel
import com.soten.fooddelivery.util.event.MenuChangeEventBus
import com.soten.fooddelivery.util.provider.ResourceProvider
import com.soten.fooddelivery.util.provider.ResourceProviderImpl
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { MyViewModel(get(), get(), get()) }
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
    viewModel { (restaurantEntity: RestaurantEntity) ->
        RestaurantDetailViewModel(
            restaurantEntity,
            get(),
            get()
        )
    }
    viewModel { (restaurantId: Long, restaurantFoodList: List<RestaurantFoodEntity>) ->
        RestaurantMenuListViewModel(
            restaurantId,
            restaurantFoodList,
            get()
        )
    }
    viewModel { (restaurantTitle: String) -> RestaurantReviewListViewModel(restaurantTitle, get()) }
    viewModel { RestaurantLikeListViewModel(get()) }
    viewModel { OrderMenuListViewModel(get(), get()) }
    viewModel { GalleryViewModel(get()) }

    single<RestaurantRepository> { RestaurantRepositoryImpl(get(), get(), get()) }
    single<MapRepository> { MapRepositoryImpl(get(), get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get(), get()) }
    single<RestaurantFoodRepository> { RestaurantFoodRepositoryImpl(get(), get(), get()) }
    single<RestaurantReviewRepository> { RestaurantReviewRepositoryImpl(get()) }
    single<OrderRepository> { OrderRepositoryImpl(get(), get()) }
    single { GalleryPhotoRepository(androidApplication()) }

    single { provideGsonConvertFactory() }
    single { buildOkHttpClient() }

    single(named("map")) { provideMapRetrofit(get(), get()) }
    single(named("food")) { provideFoodRetrofit(get(), get()) }

    single { provideMapAipService(get(qualifier = named("map"))) }
    single { provideFoodApiService(get(qualifier = named("food"))) }

    single { provideDb(androidApplication()) }
    single { provideLocationDao(get()) }
    single { provideRestaurantDao(get()) }
    single { provideFoodMenuBasketDao(get()) }
    single { AppPreferenceManager(androidApplication()) }

    single<ResourceProvider> { ResourceProviderImpl(androidApplication()) }

    // Coroutine
    single { Dispatchers.IO }
    single { Dispatchers.Main }

    single { MenuChangeEventBus() }

    single { Firebase.firestore }
    single { Firebase.auth }
    single { Firebase.storage }
}