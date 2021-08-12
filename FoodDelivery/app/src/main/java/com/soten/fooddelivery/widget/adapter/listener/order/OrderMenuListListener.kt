package com.soten.fooddelivery.widget.adapter.listener.order

import com.soten.fooddelivery.model.food.FoodModel
import com.soten.fooddelivery.widget.adapter.listener.AdapterListener

interface OrderMenuListListener : AdapterListener {

    fun onRemoveItem(model: FoodModel)

}