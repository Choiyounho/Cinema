package com.soten.shopping.di

import com.soten.shopping.data.entity.product.provideDb
import com.soten.shopping.data.entity.product.provideTodoDao
import com.soten.shopping.data.network.buildOkHttpClient
import com.soten.shopping.data.network.provideGsonConverterFactory
import com.soten.shopping.data.network.provideProductApiService
import com.soten.shopping.data.network.provideProductRetrofit
import com.soten.shopping.data.preference.PreferenceManager
import com.soten.shopping.data.repository.DefaultProductRepository
import com.soten.shopping.data.repository.ProductRepository
import com.soten.shopping.domain.*
import com.soten.shopping.presentation.detail.ProductDetailViewModel
import com.soten.shopping.presentation.list.ProductListViewModel
import com.soten.shopping.presentation.main.MainViewModel
import com.soten.shopping.presentation.profile.ProfileViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { MainViewModel() }
    viewModel { ProductListViewModel(get()) }
    viewModel { ProfileViewModel(get(), get(), get()) }
    viewModel { (productId: Long) -> ProductDetailViewModel(productId, get(), get()) }

    single { provideGsonConverterFactory() }
    single { buildOkHttpClient() }
    single { provideProductRetrofit(get(), get()) }
    single { provideProductApiService(get()) }
    single { PreferenceManager(androidContext()) }

    // Coroutines Dispatcher
    single { Dispatchers.Main }
    single { Dispatchers.IO }

    // repositories
    single<ProductRepository> { DefaultProductRepository(get(), get(), get()) }

    // UseCase
    factory { GetProductItemUseCase(get()) }
    factory { GetProductListUseCase(get()) }
    factory { OrderProductItemUseCase(get()) }
    factory { GetOrderedProductListUseCase(get()) }
    factory { DeleteOrderedProductListUseCase(get()) }

    // Database
    single { provideDb(androidContext()) }
    single { provideTodoDao(get()) }
}