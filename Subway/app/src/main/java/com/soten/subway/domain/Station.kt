package com.soten.subway.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Station(
    val name: String,
    val isFavorite: Boolean,
    val connectedSubways: List<Subway>
) : Parcelable