package com.soten.fooddelivery.widget.adapter.viewholder.order

import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.soten.fooddelivery.R
import com.soten.fooddelivery.databinding.ViewholderFoodMenuBinding
import com.soten.fooddelivery.databinding.ViewholderOrderMenuBinding
import com.soten.fooddelivery.extensions.clear
import com.soten.fooddelivery.extensions.load
import com.soten.fooddelivery.model.food.FoodModel
import com.soten.fooddelivery.screen.base.BaseViewModel
import com.soten.fooddelivery.util.provider.ResourceProvider
import com.soten.fooddelivery.widget.adapter.listener.AdapterListener
import com.soten.fooddelivery.widget.adapter.listener.order.OrderMenuListListener
import com.soten.fooddelivery.widget.adapter.listener.restaurant.FoodMenuListListener
import com.soten.fooddelivery.widget.adapter.viewholder.ModelViewHolder

class OrderMenuViewHolder(
    private val binding: ViewholderOrderMenuBinding,
    viewModel: BaseViewModel,
    resourceProvider: ResourceProvider
) : ModelViewHolder<FoodModel>(binding, viewModel, resourceProvider) {

    override fun reset() = with(binding) {
        foodImage.clear()
    }

    override fun bindData(model: FoodModel) {
        super.bindData(model)
        with(binding) {
            foodImage.load(model.imageUrl, 24f, CenterCrop())
            foodTitleText.text = model.title
            foodDescriptionText.text = model.description
            priceText.text = resourceProvider.getString(R.string.price, model.price)
        }
    }

    override fun bindViews(model: FoodModel, adapterListener: AdapterListener) {
        if (adapterListener is OrderMenuListListener) {
            binding.removeButton.setOnClickListener {
                adapterListener.onRemoveItem(model)
            }
        }
    }
}