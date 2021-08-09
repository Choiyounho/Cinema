package com.soten.fooddelivery.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MapSearchInformationEntity(
    val fullAddress: String,
    val name: String,
    val locationLatLngEntity: LocationLatLngEntity
) : Parcelable
