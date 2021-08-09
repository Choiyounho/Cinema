package com.soten.fooddelivery.screen.main.home.restaurant.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.soten.fooddelivery.data.entity.RestaurantEntity
import com.soten.fooddelivery.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantDetailViewModel(
    private val restaurantEntity: RestaurantEntity
) : BaseViewModel() {

    private val _restaurantDetailStateLiveData = MutableLiveData<RestaurantDetailState>(RestaurantDetailState.Uninitialized)
    val restaurantDetailStateLiveData: LiveData<RestaurantDetailState> =_restaurantDetailStateLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        _restaurantDetailStateLiveData.value = RestaurantDetailState.Success(
            restaurantEntity = restaurantEntity
        )
    }

    fun getRestaurantPhoneNumber(): String? {
        return when (val data = _restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                data.restaurantEntity.restaurantTelNumber
            }
            else -> null
        }
    }


}