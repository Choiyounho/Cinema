package com.soten.fooddelivery.screen.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.soten.fooddelivery.data.repository.restaurant.food.RestaurantFoodRepository
import com.soten.fooddelivery.model.CellType
import com.soten.fooddelivery.model.food.FoodModel
import com.soten.fooddelivery.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class OrderMenuListViewModel(
    private val restaurantFoodRepository: RestaurantFoodRepository
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

    fun orderMenu() {

    }

    fun orderClearMenu() {

    }

    fun removeOrderMenu(model: FoodModel) {

    }
}