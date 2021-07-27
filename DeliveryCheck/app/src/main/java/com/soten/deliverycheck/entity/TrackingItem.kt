package com.soten.deliverycheck.entity

import androidx.room.Embedded
import androidx.room.Entity

@Entity(primaryKeys = ["invoice", "code"])
data class TrackingItem(
    val invoice: String,
    @Embedded val company: ShippingCompany // ShippingCompany 내부의 멤버들이 자동 주입
)
