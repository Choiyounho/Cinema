package com.soten.fooddelivery.screen.main.like

import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.soten.fooddelivery.databinding.FragmentRestaurantLikeListBinding
import com.soten.fooddelivery.model.RestaurantModel
import com.soten.fooddelivery.screen.base.BaseFragment
import com.soten.fooddelivery.screen.main.home.restaurant.detail.RestaurantDetailActivity
import com.soten.fooddelivery.util.provider.ResourceProvider
import com.soten.fooddelivery.widget.adapter.ModelRecyclerAdapter
import com.soten.fooddelivery.widget.adapter.listener.restaurant.RestaurantLikeListListener
import org.koin.android.ext.android.inject

class RestaurantLikeListFragment : BaseFragment<RestaurantLikeListViewModel, FragmentRestaurantLikeListBinding>() {

    override val viewModel by inject<RestaurantLikeListViewModel>()

    override fun getViewBinding() = FragmentRestaurantLikeListBinding.inflate(layoutInflater)

    private val resourceProvider by inject<ResourceProvider>()

    private val adapter by lazy {
        ModelRecyclerAdapter<RestaurantModel, RestaurantLikeListViewModel>(
            modelList = listOf(),
            viewModel = viewModel,
            resourceProvider = resourceProvider,
            adapterListener = object : RestaurantLikeListListener {
                override fun onDislikeItem(model: RestaurantModel) {
                    viewModel.dislikeRestaurant(model.toEntity())
                }

                override fun onClickItem(model: RestaurantModel) {
                    startActivity(
                        RestaurantDetailActivity.newIntent(requireContext(), model.toEntity())
                    )
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchData()
    }

    override fun initViews() {
        binding.likeRecyclerView.adapter = adapter
    }

    override fun observeData() = viewModel.restaurantLikeLiveData.observe(viewLifecycleOwner) {
        checkListEmpty(it)
    }

    private fun checkListEmpty(restaurantList: List<RestaurantModel>) {
        val isEmpty = restaurantList.isEmpty()
        binding.likeRecyclerView.isGone = isEmpty
        binding.emptyResultTextView.isVisible = isEmpty
        if (isEmpty.not()) {
            adapter.submitList(restaurantList)
        }
    }

    companion object {

        const val TAG = "RestaurantLikeListFragment"

        fun newInstance() = RestaurantLikeListFragment()

    }

}