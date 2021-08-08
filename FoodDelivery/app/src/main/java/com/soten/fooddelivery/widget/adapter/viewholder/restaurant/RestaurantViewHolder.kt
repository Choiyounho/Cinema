package com.soten.fooddelivery.widget.adapter.viewholder.restaurant

import com.soten.fooddelivery.R
import com.soten.fooddelivery.databinding.VeiwholderEmptyBinding
import com.soten.fooddelivery.databinding.ViewholderRestaurantBinding
import com.soten.fooddelivery.extensions.clear
import com.soten.fooddelivery.extensions.load
import com.soten.fooddelivery.model.Model
import com.soten.fooddelivery.model.RestaurantModel
import com.soten.fooddelivery.screen.base.BaseViewModel
import com.soten.fooddelivery.util.provider.ResourceProvider
import com.soten.fooddelivery.widget.adapter.listener.AdapterListener
import com.soten.fooddelivery.widget.adapter.listener.RestaurantListListener
import com.soten.fooddelivery.widget.adapter.viewholder.ModelViewHolder

class RestaurantViewHolder(
    private val binding: ViewholderRestaurantBinding,
    viewModel: BaseViewModel,
    resourceProvider: ResourceProvider
) : ModelViewHolder<RestaurantModel>(binding, viewModel, resourceProvider) {

    override fun reset() {
        binding.restaurantImage.clear()
    }

    override fun bindData(model: RestaurantModel) {
        super.bindData(model)
        with (binding) {
            restaurantImage.load(model.restaurantImageUrl, 24f)
            restaurantTitleText.text = model.restaurantTitle
            gradeText.text = resourceProvider.getString(R.string.grade_format, model.grade)
            reviewCountText.text = resourceProvider.getString(R.string.review_count, model.reviewCount)
            val (minTime, maxTime) = model.deliveryTimeRange
            deliveryTimeText.text = resourceProvider.getString(R.string.delivery_time, minTime, maxTime)

            val (minTip, maxTip) = model.deliveryTipRange
            deliveryTipText.text = resourceProvider.getString(R.string.delivery_tip, minTip, maxTip)
        }
    }

    override fun bindViews(model: RestaurantModel, adapterListener: AdapterListener) {
        if (adapterListener is RestaurantListListener) {
            binding.root.setOnClickListener {
                adapterListener.onClickItem(model)
            }
        }
    }

}