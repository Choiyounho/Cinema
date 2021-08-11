package com.soten.fooddelivery.data.repository.restaurant.food

import com.soten.fooddelivery.data.entity.RestaurantFoodEntity
import com.soten.fooddelivery.data.network.FoodApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RestaurantFoodRepositoryImpl(
    private val foodApiService: FoodApiService,
    private val ioDispatcher: CoroutineDispatcher
) : RestaurantFoodRepository {

    override suspend fun getFoods(restaurantId: Long): List<RestaurantFoodEntity> = withContext(ioDispatcher) {
        val response = foodApiService.getRestaurantFoods(restaurantId)
        if (response.isSuccessful) {
            response.body()?.map { it.toEntity(restaurantId) } ?: listOf()
        } else {
            listOf()
        }
    }
}