package com.soten.fooddelivery.screen.main.home.restaurant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.soten.fooddelivery.data.entity.RestaurantEntity
import com.soten.fooddelivery.data.repository.RestaurantRepository
import com.soten.fooddelivery.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantListViewModel(
    private val restaurantCategory: RestaurantCategory,
    private val restaurantRepository: RestaurantRepository
): BaseViewModel() {

    private val _restaurantListLiveData = MutableLiveData<List<RestaurantEntity>>()
    val restaurantListLiveData: LiveData<List<RestaurantEntity>> = _restaurantListLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        val restaurantList = restaurantRepository.getList(restaurantCategory)
        _restaurantListLiveData.value = restaurantList
    }

}