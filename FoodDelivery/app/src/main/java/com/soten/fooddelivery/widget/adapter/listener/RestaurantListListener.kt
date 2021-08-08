package com.soten.fooddelivery.widget.adapter.listener

import com.soten.fooddelivery.model.RestaurantModel

interface RestaurantListListener: AdapterListener {

    fun onClickItem(model: RestaurantModel)

}