package com.soten.deliverycheck.presenter.trackinghistory

import com.soten.deliverycheck.data.entity.TrackingInformation
import com.soten.deliverycheck.data.entity.TrackingItem
import com.soten.deliverycheck.presenter.BasePresenter
import com.soten.deliverycheck.presenter.BaseView

class TrackingHistoryContract {

    interface View : BaseView<Presenter> {

        fun hideLoadingIndicator()

        fun showTrackingItemInformation(trackingItem: TrackingItem, trackingInformation: TrackingInformation)

        fun finish()

    }

    interface Presenter : BasePresenter {

        fun refresh()

        fun deleteTrackingItem()

    }

}