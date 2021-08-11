package com.soten.fooddelivery.screen.main.home.restaurant.detail.review

import android.widget.Toast
import androidx.core.os.bundleOf
import com.soten.fooddelivery.data.entity.RestaurantFoodEntity
import com.soten.fooddelivery.databinding.FragmentListBinding
import com.soten.fooddelivery.screen.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RestaurantReviewListFragment : BaseFragment<RestaurantReviewListViewModel, FragmentListBinding>() {

    override val viewModel by viewModel<RestaurantReviewListViewModel> {
        parametersOf(
            arguments?.getString(RESTAURANT_TITLE_KEY)
        )
    }

    override fun getViewBinding() = FragmentListBinding.inflate(layoutInflater)

    override fun observeData() = viewModel.reviewStateLiveData.observe(viewLifecycleOwner) { state ->
        when (state) {
            is RestaurantReviewState.Success -> handleSuccess(state)
        }
    }

    private fun handleSuccess(state: RestaurantReviewState.Success) {
        Toast.makeText(requireContext(), state.reviewList.toString(), Toast.LENGTH_SHORT).show()
    }

    companion object {

        const val RESTAURANT_TITLE_KEY = "restaurantTitle"

        fun newInstance(restaurantTitle: String): RestaurantReviewListFragment {
            val bundle = bundleOf(
                RESTAURANT_TITLE_KEY to restaurantTitle
            )
            return RestaurantReviewListFragment().apply {
                arguments = bundle
            }
        }

    }

}