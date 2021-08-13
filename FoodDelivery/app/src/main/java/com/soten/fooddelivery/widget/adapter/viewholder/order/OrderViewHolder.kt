package com.soten.fooddelivery.widget.adapter.viewholder.order

import com.soten.fooddelivery.R
import com.soten.fooddelivery.databinding.ViewholderOrderBinding
import com.soten.fooddelivery.model.OrderModel
import com.soten.fooddelivery.screen.base.BaseViewModel
import com.soten.fooddelivery.util.provider.ResourceProvider
import com.soten.fooddelivery.widget.adapter.listener.AdapterListener
import com.soten.fooddelivery.widget.adapter.viewholder.ModelViewHolder

class OrderViewHolder(
    private val binding: ViewholderOrderBinding,
    viewModel: BaseViewModel,
    resourceProvider: ResourceProvider
) : ModelViewHolder<OrderModel>(binding, viewModel, resourceProvider) {

    override fun reset() {}

    override fun bindData(model: OrderModel) {
        super.bindData(model)
        with(binding) {
            orderTitleText.text = resourceProvider.getString(R.string.order_history_title, model.orderId)

            val foodMenuList = model.foodMenuList

            foodMenuList
                .groupBy { it.title }
                .entries.forEach { (title, menuList) ->
                    val orderDataString = orderContentText.text.toString() +
                        "메뉴 : $title | 가격 ${menuList.first().price} X ${menuList.size}\n"
                    orderContentText.text = orderDataString
                }

            orderContentText.text = orderContentText.text.trim()

            orderTotalPriceText.text =
                resourceProvider.getString(
                    R.string.price,
                    foodMenuList.map { it.price }.reduce { total, price -> total + price }
                )
        }
    }

    override fun bindViews(model: OrderModel, adapterListener: AdapterListener) {

    }
}