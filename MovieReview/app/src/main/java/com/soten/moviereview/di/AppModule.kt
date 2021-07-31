package com.soten.moviereview.di

import android.app.Activity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.soten.moviereview.data.api.*
import com.soten.moviereview.data.preference.PreferenceManager
import com.soten.moviereview.data.preference.SharedPreferenceManager
import com.soten.moviereview.data.repository.*
import com.soten.moviereview.domain.model.Movie
import com.soten.moviereview.domain.usecase.GetAllMoviesUseCase
import com.soten.moviereview.domain.usecase.GetAllMovieReviewsUseCase
import com.soten.moviereview.domain.usecase.GetMyReviewedMoviesUseCase
import com.soten.moviereview.domain.usecase.GetRandomFeaturedMovieUseCase
import com.soten.moviereview.presenter.home.HomeContract
import com.soten.moviereview.presenter.home.HomeFragment
import com.soten.moviereview.presenter.home.HomePresenter
import com.soten.moviereview.presenter.mypage.MyPageContract
import com.soten.moviereview.presenter.mypage.MyPageFragment
import com.soten.moviereview.presenter.mypage.MyPagePresenter
import com.soten.moviereview.presenter.reviews.MovieReviewsContract
import com.soten.moviereview.presenter.reviews.MovieReviewsFragment
import com.soten.moviereview.presenter.reviews.MovieReviewsPresenter
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val appModule = module {

    single { Dispatchers.IO }

}

val dataModule = module {
    single { Firebase.firestore }

    single<MovieApi> { MovieFirestoreApi(get()) }
    single<ReviewApi> { ReviewFirestoreApi(get()) }
    single<UserApi> { UserFirestoreApi(get()) }

    single<MovieRepository> { MovieRepositoryImpl(get(), get()) }
    single<ReviewRepository> { ReviewRepositoryImpl(get(), get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get(), get()) }

    single { androidContext().getSharedPreferences("preference", Activity.MODE_PRIVATE) }
    single<PreferenceManager> { SharedPreferenceManager(get()) }
}

val domainModule = module {
    factory { GetRandomFeaturedMovieUseCase(get(), get()) }
    factory { GetAllMoviesUseCase(get()) }
    factory { GetAllMovieReviewsUseCase(get()) }
    factory { GetMyReviewedMoviesUseCase(get(), get(), get()) }
}

val presenterModule = module {
    scope<HomeFragment> {
        scoped<HomeContract.Presenter> { HomePresenter(getSource(), get(), get()) }
    }
    scope<MovieReviewsFragment> {
        scoped<MovieReviewsContract.Presenter> { (movie: Movie) ->
            MovieReviewsPresenter(
                movie,
                getSource(),
                get()
            )
        }
    }
    scope<MyPageFragment> {
        scoped<MyPageContract.Presenter> { MyPagePresenter(getSource(), get()) }
    }
}