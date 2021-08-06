package com.soten.fooddelivery.widget.adapter.viewholder

import com.soten.fooddelivery.databinding.VeiwholderEmptyBinding
import com.soten.fooddelivery.model.Model
import com.soten.fooddelivery.screen.base.BaseViewModel
import com.soten.fooddelivery.util.provider.ResourceProvider
import com.soten.fooddelivery.widget.adapter.AdapterListener

class EmptyViewHolder(
    private val binding: VeiwholderEmptyBinding,
    viewModel: BaseViewModel,
    resourceProvider: ResourceProvider
) : ModelViewHolder<Model>(binding, viewModel, resourceProvider) {

    override fun reset() {}

    override fun bindViews(model: Model, adapterListener: AdapterListener) {}

}