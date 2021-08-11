package com.soten.fooddelivery.data.response.restaurant

import com.soten.fooddelivery.data.entity.RestaurantFoodEntity

data class RestaurantFoodResponse(
    val id: String,
    val title: String,
    val description: String,
    val price: String,
    val imageUrl: String,
) {

    fun toEntity(restaurantId: Long) = RestaurantFoodEntity(
        id,
        title,
        description,
        price.toDouble().toInt(),
        imageUrl, restaurantId
    )

}
