package com.soten.fooddelivery.model.food

import com.soten.fooddelivery.model.CellType
import com.soten.fooddelivery.model.Model

data class FoodModel(
    override val id: Long,
    override val type: CellType = CellType.FOOD_CELL,
    val title: String,
    val description: String,
    val price: Int,
    val imageUrl: String,
    val restaurantId: Long
) : Model(id, type)
