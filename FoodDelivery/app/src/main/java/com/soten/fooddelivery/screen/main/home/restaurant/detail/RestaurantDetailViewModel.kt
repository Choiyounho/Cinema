package com.soten.fooddelivery.screen.main.home.restaurant.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.soten.fooddelivery.data.entity.RestaurantEntity
import com.soten.fooddelivery.data.entity.RestaurantFoodEntity
import com.soten.fooddelivery.data.repository.restaurant.food.RestaurantFoodRepository
import com.soten.fooddelivery.data.repository.user.UserRepository
import com.soten.fooddelivery.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantDetailViewModel(
    private val restaurantEntity: RestaurantEntity,
    private val restaurantFoodRepository: RestaurantFoodRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _restaurantDetailStateLiveData =
        MutableLiveData<RestaurantDetailState>(RestaurantDetailState.Uninitialized)
    val restaurantDetailStateLiveData: LiveData<RestaurantDetailState> =
        _restaurantDetailStateLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        _restaurantDetailStateLiveData.value = RestaurantDetailState.Success(
            restaurantEntity = restaurantEntity
        )
        _restaurantDetailStateLiveData.value = RestaurantDetailState.Loading
        val foods =
            restaurantFoodRepository.getFoods(restaurantId = restaurantEntity.restaurantInfoId)

        val foodMenuListInBasket = restaurantFoodRepository.getAllFoodMenuListInBasket()

        val isLiked =
            userRepository.getUserLikedRestaurant(restaurantEntity.restaurantTitle) != null
        _restaurantDetailStateLiveData.value = RestaurantDetailState.Success(
            restaurantEntity = restaurantEntity,
            restaurantFoodList = foods,
            isLiked = isLiked,
            foodMenuListInBasket = foodMenuListInBasket
        )
    }

    fun getRestaurantTelNumber(): String? {
        return when (val data = _restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                data.restaurantEntity.restaurantTelNumber
            }
            else -> null
        }
    }

    fun getRestaurantInformation(): RestaurantEntity? {
        return when (val data = _restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                data.restaurantEntity
            }
            else -> null
        }
    }

    fun toggleLikeRestaurant() = viewModelScope.launch {
        when (val data = _restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                userRepository.getUserLikedRestaurant(restaurantEntity.restaurantTitle)?.let {
                    userRepository.deleteUserLikedRestaurant(it.restaurantTitle)
                    _restaurantDetailStateLiveData.value = data.copy(
                        isLiked = false
                    )
                } ?: run {
                    userRepository.insertUserLikedRestaurant(restaurantEntity)
                    _restaurantDetailStateLiveData.value = data.copy(
                        isLiked = true
                    )
                }
            }
        }
    }

    fun notifyFoodMenuListInBasket(restaurantFoodEntity: RestaurantFoodEntity) =
        viewModelScope.launch {
            when (val data = _restaurantDetailStateLiveData.value) {
                is RestaurantDetailState.Success -> {
                    _restaurantDetailStateLiveData.value = data.copy(
                        foodMenuListInBasket = data.foodMenuListInBasket?.toMutableList()?.apply {
                            add(restaurantFoodEntity)
                        }
                    )
                }
                else -> {
                }
            }
        }

    fun notifyClearNeedAlertInBasket(clearNeed: Boolean, afterAction: () -> Unit) = viewModelScope.launch {
        when (val data = _restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                _restaurantDetailStateLiveData.value = data.copy(
                    isClearNeedInBasketAndAction = Pair(clearNeed, afterAction)
                )
            }
            else -> {
            }
        }
    }

    fun notifyClearBasket() = viewModelScope.launch {
        when (val data = _restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                _restaurantDetailStateLiveData.value = data.copy(
                    foodMenuListInBasket = listOf(),
                    isClearNeedInBasketAndAction = Pair(false, {})
                )
            }
            else -> {
            }
        }
    }

}