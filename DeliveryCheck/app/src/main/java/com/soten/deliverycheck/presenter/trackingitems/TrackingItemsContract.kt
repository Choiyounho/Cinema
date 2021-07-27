package com.soten.deliverycheck.presenter.trackingitems

import com.soten.deliverycheck.entity.TrackingInformation
import com.soten.deliverycheck.entity.TrackingItem
import com.soten.deliverycheck.presenter.BasePresenter
import com.soten.deliverycheck.presenter.BaseView

class TrackingItemsContract {

    interface View : BaseView<Presenter> {

        fun showLoadingIndicator()

        fun hideLoadingIndicator()

        fun showNoDataDescription()

        fun showTrackingItemInformation(trackingItemInformation: List<Pair<TrackingItem, TrackingInformation>>)
    }

    interface Presenter : BasePresenter {

        var trackingItemInformation: List<Pair<TrackingItem, TrackingInformation>>

        fun refresh()

    }

}