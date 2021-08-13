package com.soten.fooddelivery.widget.adapter.listener.order

import com.soten.fooddelivery.widget.adapter.listener.AdapterListener

interface OrderListListener : AdapterListener {

    fun writeRestaurantReview(orderId: String, restaurantTitle: String)

}