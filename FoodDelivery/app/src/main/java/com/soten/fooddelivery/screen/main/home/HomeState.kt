package com.soten.fooddelivery.screen.main.home

import androidx.annotation.StringRes
import com.soten.fooddelivery.data.entity.LocationLatLngEntity
import com.soten.fooddelivery.data.entity.MapSearchInformationEntity

sealed class HomeState {

    object Uninitialized: HomeState()

    object Loading: HomeState()

    data class Success(
        val mapSearchInformationEntity: MapSearchInformationEntity,
        val isLocationSame: Boolean
    ) : HomeState()

    data class Error(
        @StringRes val messageId: Int
    ) : HomeState()
}
