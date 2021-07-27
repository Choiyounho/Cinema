package com.soten.deliverycheck.presenter.trackingitems

import com.soten.deliverycheck.data.entity.TrackingInformation
import com.soten.deliverycheck.data.entity.TrackingItem
import com.soten.deliverycheck.repository.TrackingItemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TrackingItemsPresenter(
    private val view: TrackingItemsContract.View,
    private val trackingItemRepository: TrackingItemRepository
) : TrackingItemsContract.Presenter {

    override var trackingItemInformation: List<Pair<TrackingItem, TrackingInformation>> = emptyList()

    override val scope: CoroutineScope = MainScope()

    init {
        scope.launch {
            trackingItemRepository
                .trackingItems
                .collect { refresh() } // 변경이 되면 목록 업데이트 Observable
        }
    }

    override fun onViewCreated() {
        fetchTrackingInformation()
    }

    override fun onDestroyView() {}

    override fun refresh() {
        fetchTrackingInformation(true)
    }

    // 스와이프리프레쉬를 이용하지 않았을 경우에는 데이터 갱신을 하지 않는다.
    private fun fetchTrackingInformation(forceFetch: Boolean = false) = scope.launch {
        try {
            view.showLoadingIndicator()

            if (trackingItemInformation.isEmpty() || forceFetch) {
                trackingItemInformation = trackingItemRepository.getTrackingItemInformation()
            }

            if (trackingItemInformation.isEmpty()) {
                view.showNoDataDescription()
            } else {
                view.showTrackingItemInformation(trackingItemInformation)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            view.hideLoadingIndicator()
        }
    }
}