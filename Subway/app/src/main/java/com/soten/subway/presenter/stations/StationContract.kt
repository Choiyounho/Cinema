package com.soten.subway.presenter.stations

import com.soten.subway.domain.Station
import com.soten.subway.presenter.BasePresenter
import com.soten.subway.presenter.BaseView

interface StationContract {

    interface View : BaseView<Presenter> {

        fun showLoadingIndicator()

        fun hideLoadingIndicator()

        fun showStations(stations: List<Station>)

    }

    interface Presenter : BasePresenter {

        fun filterStations(query: String)

        fun toggleStationFavorite(station: Station)

    }

}