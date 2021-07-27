package com.soten.deliverycheck.presenter

interface BaseView<PresenterT : BasePresenter> {

    val presenter: PresenterT

}