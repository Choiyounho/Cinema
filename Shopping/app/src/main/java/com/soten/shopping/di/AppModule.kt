package com.soten.shopping.di

import com.soten.shopping.data.network.buildOkHttpClient
import com.soten.shopping.data.network.provideGsonConverterFactory
import com.soten.shopping.data.network.provideProductApiService
import com.soten.shopping.data.network.provideProductRetrofit
import com.soten.shopping.data.repository.DefaultProductRepository
import com.soten.shopping.data.repository.ProductRepository
import com.soten.shopping.domain.GetProductItemUseCase
import com.soten.shopping.domain.GetProductListUseCase
import com.soten.shopping.presentation.list.ProductListViewModel
import com.soten.shopping.presentation.main.MainViewModel
import com.soten.shopping.presentation.profile.ProfileViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { MainViewModel() }
    viewModel { ProductListViewModel(get()) }
    viewModel { ProfileViewModel() }

    single { provideGsonConverterFactory() }
    single { buildOkHttpClient() }
    single { provideProductRetrofit(get(), get()) }
    single { provideProductApiService(get()) }

    // Coroutines Dispatcher
    single { Dispatchers.Main }
    single { Dispatchers.IO }

    // repositories
    single<ProductRepository> { DefaultProductRepository(get(), get()) }

    // UseCase
    factory { GetProductItemUseCase(get()) }
    factory { GetProductListUseCase(get()) }
}