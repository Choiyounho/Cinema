package com.soten.fooddelivery.widget.adapter.listener.restaurant

import com.soten.fooddelivery.model.RestaurantModel
import com.soten.fooddelivery.widget.adapter.listener.AdapterListener

interface RestaurantListListener: AdapterListener {

    fun onClickItem(model: RestaurantModel)

}