package com.soten.fooddelivery.screen.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.soten.fooddelivery.R
import com.soten.fooddelivery.data.repository.order.OrderRepository
import com.soten.fooddelivery.data.repository.order.OrderResult
import com.soten.fooddelivery.data.repository.restaurant.food.RestaurantFoodRepository
import com.soten.fooddelivery.model.CellType
import com.soten.fooddelivery.model.food.FoodModel
import com.soten.fooddelivery.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class OrderMenuListViewModel(
    private val restaurantFoodRepository: RestaurantFoodRepository,
    private val orderRepository: OrderRepository
) : BaseViewModel() {

    private val _orderMenuStateLiveData = MutableLiveData<OrderMenuState>(OrderMenuState.Uninitialized)
    val orderMenuStateLiveData: LiveData<OrderMenuState> = _orderMenuStateLiveData

    private val auth by lazy { Firebase.auth }

    override fun fetchData(): Job = viewModelScope.launch {
        _orderMenuStateLiveData.value = OrderMenuState.Loading
        val foodMenuList = restaurantFoodRepository.getAllFoodMenuListInBasket()
        _orderMenuStateLiveData.value = OrderMenuState.Success(
            foodMenuList.map {
                FoodModel(
                    id = it.hashCode().toLong(),
                    type = CellType.ORDER_FOOD_CELL,
                    title = it.title,
                    description = it.description,
                    price = it.price,
                    imageUrl = it.imageUrl,
                    restaurantId = it.restaurantId,
                    foodId = it.id,
                )
            }
        )
    }

    fun orderMenu() = viewModelScope.launch {
        val foodMenuList = restaurantFoodRepository.getAllFoodMenuListInBasket()
        if (foodMenuList.isNotEmpty()) {
            val restaurantId = foodMenuList.first().restaurantId
            auth.currentUser?.let { user ->
                when(val data = orderRepository.orderMenu(user.uid, restaurantId, foodMenuList)) {
                    is OrderResult.Success<*> -> {
                        restaurantFoodRepository.clearFoodMenuListInBasket()
                        _orderMenuStateLiveData.value = OrderMenuState.Order
                    }
                    is OrderResult.Error -> {
                        _orderMenuStateLiveData.value = OrderMenuState.Error(
                            R.string.error_message, data.exception
                        )
                    }
                }
            } ?: run {
                _orderMenuStateLiveData.value = OrderMenuState.Error(
                    R.string.user_id_not_found, IllegalAccessException()
                )
            }
        }
    }

    fun orderClearMenu() = viewModelScope.launch {
        restaurantFoodRepository.clearFoodMenuListInBasket()
    }

    fun removeOrderMenu(model: FoodModel) = viewModelScope.launch {
        restaurantFoodRepository.removeFoodMenuListInBasket(model.foodId)
        fetchData()
    }
}