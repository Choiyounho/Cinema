package com.soten.moviereview.di

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.soten.moviereview.data.api.MovieApi
import com.soten.moviereview.data.api.MovieFirestoreApi
import com.soten.moviereview.data.api.ReviewApi
import com.soten.moviereview.data.api.ReviewFirestoreApi
import com.soten.moviereview.data.repository.MovieRepository
import com.soten.moviereview.data.repository.MovieRepositoryImpl
import com.soten.moviereview.data.repository.ReviewRepository
import com.soten.moviereview.data.repository.ReviewRepositoryImpl
import com.soten.moviereview.domain.usecase.GetAllMoviesUseCase
import com.soten.moviereview.domain.usecase.GetRandomFeaturedMovieUseCase
import com.soten.moviereview.presenter.home.HomeContract
import com.soten.moviereview.presenter.home.HomeFragment
import com.soten.moviereview.presenter.home.HomePresenter
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module


val appModule = module {

    single { Dispatchers.IO }

}

val dataModule = module {
    single { Firebase.firestore }

    single<MovieApi> { MovieFirestoreApi(get()) }
    single<ReviewApi> { ReviewFirestoreApi(get()) }

    single<MovieRepository> { MovieRepositoryImpl(get(), get()) }
    single<ReviewRepository> { ReviewRepositoryImpl(get(), get()) }
}

val domainModule = module {
    factory { GetRandomFeaturedMovieUseCase(get(), get()) }
    factory { GetAllMoviesUseCase(get()) }
}

val presenterModule = module {
    scope<HomeFragment> {
        scoped<HomeContract.Presenter> { HomePresenter(getSource(), get(), get()) }
    }
}