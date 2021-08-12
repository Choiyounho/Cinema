package com.soten.fooddelivery.screen.order

import android.content.Context
import android.content.Intent
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import com.soten.fooddelivery.databinding.ActivityOrderMenuListBinding
import com.soten.fooddelivery.model.food.FoodModel
import com.soten.fooddelivery.screen.base.BaseActivity
import com.soten.fooddelivery.util.provider.ResourceProvider
import com.soten.fooddelivery.widget.adapter.ModelRecyclerAdapter
import com.soten.fooddelivery.widget.adapter.listener.order.OrderMenuListListener
import org.koin.android.ext.android.inject

class OrderMenuListActivity : BaseActivity<OrderMenuListViewModel, ActivityOrderMenuListBinding>() {

    override val viewModel by inject<OrderMenuListViewModel>()

    override fun getViewBinding() = ActivityOrderMenuListBinding.inflate(layoutInflater)

    private val resourceProvider by inject<ResourceProvider>()

    private val adapter by lazy {
        ModelRecyclerAdapter<FoodModel, OrderMenuListViewModel>(
            listOf(),
            viewModel,
            resourceProvider,
            adapterListener = object : OrderMenuListListener {
                override fun onRemoveItem(model: FoodModel) {
                    viewModel.removeOrderMenu(model)
                }
            }
        )
    }

    override fun initViews() = with(binding) {
        orderRecyclerView.adapter = adapter
        toolbar.setNavigationOnClickListener { finish() }
        confirmButton.setOnClickListener {
            viewModel.orderMenu()
        }
        orderClearButton.setOnClickListener {
            viewModel.orderClearMenu()
        }
    }

    override fun observeData() = viewModel.orderMenuStateLiveData.observe(this) { state ->
        when (state) {
            is OrderMenuState.Loading -> handleLoading()
            is OrderMenuState.Success -> handleSuccess(state)
            is OrderMenuState.Order -> {
            }
            is OrderMenuState.Error -> {
            }
            else -> {
            }
        }
    }

    private fun handleLoading() = with(binding) {
        progressBar.isVisible = true
    }

    private fun handleSuccess(state: OrderMenuState.Success) = with(binding) {
        progressBar.isGone = true
        val menuOrderIsEmpty = state.restaurantFoodModelList.isNullOrEmpty()
        confirmButton.isEnabled = menuOrderIsEmpty.not()
        if (menuOrderIsEmpty) {
            Snackbar.make(binding.root, "주문 메뉴가 없어 화면이 종료됩니다.", Snackbar.LENGTH_SHORT).show()
            finish()
        }
        adapter.submitList(state.restaurantFoodModelList)
    }

    companion object {

        fun newIntent(context: Context) = Intent(context, OrderMenuListActivity::class.java)

    }

}