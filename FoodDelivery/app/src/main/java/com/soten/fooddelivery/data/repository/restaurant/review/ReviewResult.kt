package com.soten.fooddelivery.data.repository.restaurant.review

sealed class ReviewResult {

    data class Success<T>(
        val data: T? = null
    ) : ReviewResult()

    data class Error(
        val exception: Throwable
    ) : ReviewResult()

}
