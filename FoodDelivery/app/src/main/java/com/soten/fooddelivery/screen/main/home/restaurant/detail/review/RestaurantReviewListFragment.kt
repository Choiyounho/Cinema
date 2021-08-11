package com.soten.fooddelivery.screen.main.home.restaurant.detail.review

import androidx.core.os.bundleOf
import com.soten.fooddelivery.data.entity.RestaurantFoodEntity
import com.soten.fooddelivery.databinding.FragmentListBinding
import com.soten.fooddelivery.screen.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class RestaurantReviewListFragment : BaseFragment<RestaurantReviewListViewModel, FragmentListBinding>() {

    override val viewModel by viewModel<RestaurantReviewListViewModel>()

    override fun getViewBinding() = FragmentListBinding.inflate(layoutInflater)

    override fun observeData() {
    }

    companion object {

        const val RESTAURANT_ID_KEY = "restaurantId"

        fun newInstance(restaurantId: Long): RestaurantReviewListFragment {
            val bundle = bundleOf(
                RESTAURANT_ID_KEY to restaurantId,
            )
            return RestaurantReviewListFragment().apply {
                arguments = bundle
            }
        }

    }

}