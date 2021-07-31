package com.soten.moviereview.presenter.reviews

import com.soten.moviereview.domain.model.Movie
import com.soten.moviereview.domain.model.Review
import com.soten.moviereview.presenter.BasePresenter
import com.soten.moviereview.presenter.BaseView

interface MovieReviewsContract {

    interface View: BaseView<Presenter> {

        fun showLoadingIndicator()

        fun hideLoadingIndicator()

        fun showErrorDescription(message: String)

        fun showMovieInformation(movie: Movie)

        fun showReviews(reviews: List<Review>)

    }

    interface Presenter: BasePresenter {

        val movie: Movie

    }
}