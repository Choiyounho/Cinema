package com.soten.moviereview.presenter.mypage

import com.soten.moviereview.domain.model.ReviewedMovie
import com.soten.moviereview.presenter.BasePresenter
import com.soten.moviereview.presenter.BaseView

interface MyPageContract {

    interface View : BaseView<Presenter> {

        fun showLoadingIndicator()

        fun hideLoadingIndicator()

        fun showNoDataDescription(message: String)

        fun showErrorDescription(message: String)

        fun showReviewedMovies(reviewedMovies: List<ReviewedMovie>)
    }

    interface Presenter : BasePresenter
}