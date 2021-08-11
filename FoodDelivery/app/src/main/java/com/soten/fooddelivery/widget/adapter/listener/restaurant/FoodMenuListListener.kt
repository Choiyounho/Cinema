package com.soten.fooddelivery.widget.adapter.listener.restaurant

import com.soten.fooddelivery.model.food.FoodModel
import com.soten.fooddelivery.widget.adapter.listener.AdapterListener

interface FoodMenuListListener : AdapterListener {

    fun onClickItem(model: FoodModel)

}