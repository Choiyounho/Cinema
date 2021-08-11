package com.soten.fooddelivery.data.repository.restaurant.food

import com.soten.fooddelivery.data.entity.RestaurantFoodEntity

interface RestaurantFoodRepository {

    suspend fun getFoods(restaurantId: Long): List<RestaurantFoodEntity>

}