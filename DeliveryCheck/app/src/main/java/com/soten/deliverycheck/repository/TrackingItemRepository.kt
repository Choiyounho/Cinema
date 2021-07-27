package com.soten.deliverycheck.repository

import com.soten.deliverycheck.entity.TrackingInformation
import com.soten.deliverycheck.entity.TrackingItem

interface TrackingItemRepository {

    suspend fun getTrackingItemInformation(): List<Pair<TrackingItem, TrackingInformation>>

}