package com.soten.fooddelivery.screen.main.home.restaurant

import android.widget.Toast
import androidx.core.os.bundleOf
import com.soten.fooddelivery.databinding.FragmentRestaurantListBinding
import com.soten.fooddelivery.model.RestaurantModel
import com.soten.fooddelivery.screen.base.BaseFragment
import com.soten.fooddelivery.util.provider.ResourceProvider
import com.soten.fooddelivery.widget.adapter.ModelRecyclerAdapter
import com.soten.fooddelivery.widget.adapter.listener.AdapterListener
import com.soten.fooddelivery.widget.adapter.listener.RestaurantListListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RestaurantListFragment :
    BaseFragment<RestaurantListViewModel, FragmentRestaurantListBinding>() {

    private val restaurantCategory by lazy { arguments?.getSerializable(RESTAURANT_CATEGORY_KEY) as RestaurantCategory }

    override val viewModel by viewModel<RestaurantListViewModel> { parametersOf(restaurantCategory) }

    override fun getViewBinding(): FragmentRestaurantListBinding =
        FragmentRestaurantListBinding.inflate(layoutInflater)

    private val resourceProvider by inject<ResourceProvider>()

    private val adapter by lazy {
        ModelRecyclerAdapter<RestaurantModel, RestaurantListViewModel>(
            listOf(),
            viewModel,
            resourceProvider,
            object : RestaurantListListener, AdapterListener {
                override fun onClickItem(model: RestaurantModel) {
                    Toast.makeText(requireContext(), "$model", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun initViews() = with(binding) {
        restaurantListRecyclerView.adapter = adapter
    }

    override fun observeData() = viewModel.restaurantListLiveData.observe(viewLifecycleOwner) {
        adapter.submitList(it)
    }

    companion object {
        const val RESTAURANT_CATEGORY_KEY = "restaurantCategory"

        fun newInstance(restaurantCategory: RestaurantCategory): RestaurantListFragment {
            return RestaurantListFragment().apply {
                arguments = bundleOf(
                    RESTAURANT_CATEGORY_KEY to restaurantCategory
                )
            }
        }
    }
}