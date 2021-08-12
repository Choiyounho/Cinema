package com.soten.fooddelivery.widget.adapter.viewholder.review

import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.soten.fooddelivery.databinding.ViewholderRestaurantReviewBinding
import com.soten.fooddelivery.extensions.clear
import com.soten.fooddelivery.extensions.load
import com.soten.fooddelivery.model.review.RestaurantReviewModel
import com.soten.fooddelivery.screen.base.BaseViewModel
import com.soten.fooddelivery.util.provider.ResourceProvider
import com.soten.fooddelivery.widget.adapter.listener.AdapterListener
import com.soten.fooddelivery.widget.adapter.viewholder.ModelViewHolder

class RestaurantReviewViewHolder(
    private val binding: ViewholderRestaurantReviewBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourceProvider
) : ModelViewHolder<RestaurantReviewModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = with(binding) {
        reviewThumbnailImage.clear()
        reviewThumbnailImage.isGone = true
    }

    override fun bindData(model: RestaurantReviewModel) {
        super.bindData(model)
        with(binding) {
            if (model.thumbnailImageUri != null) {
                reviewThumbnailImage.isVisible = true
                reviewThumbnailImage.load(model.thumbnailImageUri.toString())
            } else {
                reviewThumbnailImage.isGone = true
            }

            reviewTitleText.text = model.title
            reviewText.text = model.description
            ratingBar.rating = model.grade.toFloat()
        }
    }

    override fun bindViews(model: RestaurantReviewModel, adapterListener: AdapterListener) {}

}