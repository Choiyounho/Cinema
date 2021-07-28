package com.soten.deliverycheck.data.entity

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(primaryKeys = ["invoice", "code"])
data class TrackingItem(
    val invoice: String,
    @Embedded val company: ShippingCompany
) : Parcelable
// ShippingCompany 내부의 멤버들이 자동 주입