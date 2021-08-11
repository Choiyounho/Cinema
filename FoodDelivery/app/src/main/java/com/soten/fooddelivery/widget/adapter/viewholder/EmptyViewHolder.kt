package com.soten.fooddelivery.widget.adapter.viewholder

import com.soten.fooddelivery.databinding.ViewholderEmptyBinding
import com.soten.fooddelivery.model.Model
import com.soten.fooddelivery.screen.base.BaseViewModel
import com.soten.fooddelivery.util.provider.ResourceProvider
import com.soten.fooddelivery.widget.adapter.listener.AdapterListener

class EmptyViewHolder(
    private val binding: ViewholderEmptyBinding,
    viewModel: BaseViewModel,
    resourceProvider: ResourceProvider
) : ModelViewHolder<Model>(binding, viewModel, resourceProvider) {

    override fun reset() {}

    override fun bindViews(model: Model, adapterListener: AdapterListener) {}

}