package com.soten.fooddelivery.data.repository.order

import com.soten.fooddelivery.data.entity.RestaurantFoodEntity

interface OrderRepository {

    suspend fun orderMenu(
        userId: String,
        restaurantId: Long,
        foodMenuList: List<RestaurantFoodEntity>,
        restaurantTitle: String
    ): OrderResult

    suspend fun getAllOrderMenus(
        userId: String
    ): OrderResult

}