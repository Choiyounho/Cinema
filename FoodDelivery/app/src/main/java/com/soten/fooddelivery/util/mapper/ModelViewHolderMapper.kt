package com.soten.fooddelivery.util.mapper

import android.view.LayoutInflater
import android.view.ViewGroup
import com.soten.fooddelivery.databinding.ViewholderEmptyBinding
import com.soten.fooddelivery.databinding.ViewholderFoodMenuBinding
import com.soten.fooddelivery.databinding.ViewholderRestaurantBinding
import com.soten.fooddelivery.model.CellType
import com.soten.fooddelivery.model.Model
import com.soten.fooddelivery.screen.base.BaseViewModel
import com.soten.fooddelivery.util.provider.ResourceProvider
import com.soten.fooddelivery.widget.adapter.viewholder.EmptyViewHolder
import com.soten.fooddelivery.widget.adapter.viewholder.ModelViewHolder
import com.soten.fooddelivery.widget.adapter.viewholder.food.FoodMenuViewHolder
import com.soten.fooddelivery.widget.adapter.viewholder.restaurant.RestaurantViewHolder

object ModelViewHolderMapper {

    @Suppress("UNCHECKED_CAST")
    fun <M : Model> map(
        parent: ViewGroup,
        type: CellType,
        viewModel: BaseViewModel,
        resourceProvider: ResourceProvider
    ): ModelViewHolder<M> {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = when (type) {
            CellType.EMPTY_CELL -> EmptyViewHolder(
                ViewholderEmptyBinding.inflate(inflater, parent, false),
                viewModel,
                resourceProvider
            )
            CellType.RESTAURANT_CELL -> RestaurantViewHolder(
                ViewholderRestaurantBinding.inflate(inflater, parent, false),
                viewModel,
                resourceProvider
            )
            CellType.FOOD_CELL -> FoodMenuViewHolder(
                ViewholderFoodMenuBinding.inflate(inflater, parent, false),
                viewModel,
                resourceProvider
            )
        }
        return viewHolder as ModelViewHolder<M>
    }

}