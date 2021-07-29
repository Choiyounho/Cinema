package com.soten.moviereview.presenter

interface BaseView<PresenterT : BasePresenter> {

    val presenter: PresenterT

}