package com.soten.deliverycheck.repository

import com.soten.deliverycheck.data.api.SweetTrackerApi
import com.soten.deliverycheck.data.db.TrackingItemDao
import com.soten.deliverycheck.data.entity.TrackingInformation
import com.soten.deliverycheck.data.entity.TrackingItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class TrackingRepositoryImpl(
    private val trackerApi: SweetTrackerApi,
    private val trackingItemDao: TrackingItemDao,
    private val dispatcher: CoroutineDispatcher
) : TrackingItemRepository {

    override suspend fun getTrackingItemInformation(): List<Pair<TrackingItem, TrackingInformation>> =
        withContext(dispatcher) {
            trackingItemDao.getAll() // 트래킹 아이템을 가져옴
                .mapNotNull { trackingItem ->
                    val relatedTrackingInfo = trackerApi.getTrackingInformation(
                        trackingItem.company.code,
                        trackingItem.invoice
                    ).body() // company code, invoice 를 가져와서 아이템 마다 호출

                    if (relatedTrackingInfo?.invoiceNo.isNullOrBlank()) { // invoiceNo 가 존재하지 않을 경우 데이터 유효 x 처리
                        null
                    } else {
                        trackingItem to relatedTrackingInfo!!
                    }
                }
                .sortedWith(
                    compareBy(
                        { it.second.level },
                        { -(it.second.lastDetail?.time ?: Long.MAX_VALUE) }
                    )
                )
        }
}