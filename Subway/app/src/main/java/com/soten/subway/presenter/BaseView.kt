package com.soten.subway.presenter

interface BaseView<PresenterT : BasePresenter> {

    val presenter: PresenterT

}