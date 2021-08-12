package com.soten.fooddelivery.screen.order

import androidx.annotation.StringRes
import com.soten.fooddelivery.model.food.FoodModel

sealed class OrderMenuState {

    object Uninitialized: OrderMenuState()

    object Loading: OrderMenuState()

    data class Success(
        val restaurantFoodModelList: List<FoodModel>? = null
    ): OrderMenuState()

    object Order: OrderMenuState()

    data class Error(
        @StringRes val messageId: Int,
        val e: Throwable
    ): OrderMenuState()


}
