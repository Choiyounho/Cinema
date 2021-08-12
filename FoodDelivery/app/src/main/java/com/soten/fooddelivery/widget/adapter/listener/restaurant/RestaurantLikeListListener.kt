package com.soten.fooddelivery.widget.adapter.listener.restaurant

import com.soten.fooddelivery.model.RestaurantModel

interface RestaurantLikeListListener : RestaurantListListener {

    fun onDislikeItem(model: RestaurantModel)

}