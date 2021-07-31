package com.soten.moviereview.presenter.home

import com.soten.moviereview.domain.model.FeaturedMovie
import com.soten.moviereview.domain.model.Movie
import com.soten.moviereview.presenter.BasePresenter
import com.soten.moviereview.presenter.BaseView

interface HomeContract {

    interface View: BaseView<Presenter> {

        fun showLoadingIndicator()

        fun hideLoadingIndicator()

        fun showErrorDescription(message: String)

        fun showMovies(
            featuredMovie: FeaturedMovie?,
            movies: List<Movie>
        )

    }

    interface Presenter: BasePresenter

}