package com.soten.fooddelivery.data.repository.restaurant.review

import com.soten.fooddelivery.data.entity.RestaurantReviewEntity

interface RestaurantReviewRepository {

    suspend fun getReviews(restaurantTitle: String): List<RestaurantReviewEntity>

}