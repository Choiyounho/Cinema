package com.soten.fooddelivery.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class LocationLatLngEntity(
    @PrimaryKey(autoGenerate = true)
    override val id: Long = -1,
    val latitude: Double,
    val longitude: Double
) : com.soten.fooddelivery.data.entity.Entity, Parcelable
