package com.soten.fooddelivery.screen.main.home.restaurant.detail.review

import android.widget.Toast
import androidx.core.os.bundleOf
import com.soten.fooddelivery.data.entity.RestaurantFoodEntity
import com.soten.fooddelivery.databinding.FragmentListBinding
import com.soten.fooddelivery.model.review.RestaurantReviewModel
import com.soten.fooddelivery.screen.base.BaseFragment
import com.soten.fooddelivery.util.provider.ResourceProvider
import com.soten.fooddelivery.widget.adapter.ModelRecyclerAdapter
import com.soten.fooddelivery.widget.adapter.listener.AdapterListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RestaurantReviewListFragment : BaseFragment<RestaurantReviewListViewModel, FragmentListBinding>() {

    override val viewModel by viewModel<RestaurantReviewListViewModel> {
        parametersOf(
            arguments?.getString(RESTAURANT_TITLE_KEY)
        )
    }
    private val resourceProvider by inject<ResourceProvider>()

    override fun getViewBinding() = FragmentListBinding.inflate(layoutInflater)

    private val adapter by lazy {
        ModelRecyclerAdapter<RestaurantReviewModel, RestaurantReviewListViewModel>(
            listOf(),
            viewModel,
            resourceProvider,
            object : AdapterListener {}
        )
    }

    override fun initViews() {
        binding.menuRecyclerView.adapter = adapter
    }

    override fun observeData() = viewModel.reviewStateLiveData.observe(viewLifecycleOwner) { state ->
        when (state) {
            is RestaurantReviewState.Success -> handleSuccess(state)
        }
    }

    private fun handleSuccess(state: RestaurantReviewState.Success) {
        adapter.submitList(state.reviewList)
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