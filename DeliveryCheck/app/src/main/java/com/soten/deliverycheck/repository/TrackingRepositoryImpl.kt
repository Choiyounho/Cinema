package com.soten.deliverycheck.repository

import com.soten.deliverycheck.data.api.SweetTrackerApi
import com.soten.deliverycheck.data.db.TrackingItemDao
import com.soten.deliverycheck.data.entity.TrackingInformation
import com.soten.deliverycheck.data.entity.TrackingItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.withContext
import java.lang.RuntimeException

class TrackingRepositoryImpl(
    private val trackerApi: SweetTrackerApi,
    private val trackingItemDao: TrackingItemDao,
    private val dispatcher: CoroutineDispatcher
) : TrackingItemRepository {

    override val trackingItems: Flow<List<TrackingItem>> =
        trackingItemDao.allTrackingItems().distinctUntilChanged()

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

    override suspend fun getTrackingInformation(
        companyCode: String,
        invoice: String
    ): TrackingInformation? =
        trackerApi.getTrackingInformation(companyCode, invoice)
            .body()
            ?.sortTrackingDetailsByTimeDescending()

    override suspend fun saveTrackingItem(trackingItem: TrackingItem) = withContext(dispatcher) {
        val trackingInformation = trackerApi.getTrackingInformation(
            trackingItem.company.code,
            trackingItem.invoice
        ).body()

        if (!trackingInformation!!.errorMessage.isNullOrBlank()) {
            throw RuntimeException(trackingInformation.errorMessage)
        }

        trackingItemDao.insert(trackingItem)
    }

    override suspend fun deleteTrackingItem(trackingItem: TrackingItem) {
        trackingItemDao.delete(trackingItem)
    }

    private fun TrackingInformation.sortTrackingDetailsByTimeDescending() =
        copy(trackingDetails = trackingDetails?.sortedByDescending { it.time ?: 0L })

}
