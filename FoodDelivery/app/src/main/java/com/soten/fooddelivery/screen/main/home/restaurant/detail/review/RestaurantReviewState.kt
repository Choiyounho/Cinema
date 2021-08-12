package com.soten.fooddelivery.screen.main.home.restaurant.detail.review

import com.soten.fooddelivery.model.review.RestaurantReviewModel

sealed class RestaurantReviewState {

    object Uninitialized: RestaurantReviewState()

    object Loading: RestaurantReviewState()

    data class Success(
        val reviewList: List<RestaurantReviewModel>
    ): RestaurantReviewState()

}
