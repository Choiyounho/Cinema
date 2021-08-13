package com.soten.fooddelivery.data.repository.restaurant.review

interface RestaurantReviewRepository {

    suspend fun getReviews(restaurantTitle: String): ReviewResult

}