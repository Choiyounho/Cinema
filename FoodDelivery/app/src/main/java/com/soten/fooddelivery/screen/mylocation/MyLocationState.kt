package com.soten.fooddelivery.screen.mylocation

import androidx.annotation.StringRes
import com.soten.fooddelivery.data.entity.MapSearchInformationEntity

sealed class MyLocationState {

    object Uninitialized: MyLocationState()

    object Loading: MyLocationState()

    data class Success(
        val mapSearchInformationEntity: MapSearchInformationEntity
    ) : MyLocationState()

    data class Confirm(
        val mapSearchInformationEntity: MapSearchInformationEntity
    ) : MyLocationState()

    data class Error(
        @StringRes val messageId: Int
    ) : MyLocationState()

}