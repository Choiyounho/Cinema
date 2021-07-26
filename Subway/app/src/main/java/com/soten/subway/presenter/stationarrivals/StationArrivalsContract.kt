package com.soten.subway.presenter.stationarrivals

import com.soten.subway.domain.ArrivalInformation
import com.soten.subway.presenter.BasePresenter
import com.soten.subway.presenter.BaseView

interface StationArrivalsContract {

    interface View : BaseView<Presenter> {

        fun showLoadingIndicator()

        fun hideLoadingIndicator()

        fun showErrorDescription(message: String)

        fun showStationArrivals(arrivalInformation: List<ArrivalInformation>)

    }

    interface Presenter : BasePresenter {

        fun fetchStationArrivals()

        fun toggleStationFavorite()

    }
}