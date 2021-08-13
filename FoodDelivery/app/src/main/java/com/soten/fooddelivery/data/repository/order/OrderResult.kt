package com.soten.fooddelivery.data.repository.order

sealed class OrderResult {
    data class Success<T>(
        val data: T? = null
    ) : OrderResult()

    data class Error(
        val exception: Throwable
    ) : OrderResult()
}
