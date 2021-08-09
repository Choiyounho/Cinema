package com.soten.fooddelivery.screen.main.home.restaurant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.soten.fooddelivery.data.entity.LocationLatLngEntity
import com.soten.fooddelivery.data.repository.restaurant.RestaurantRepository
import com.soten.fooddelivery.model.RestaurantModel
import com.soten.fooddelivery.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantListViewModel(
    private val restaurantCategory: RestaurantCategory,
    private var locationLatLngEntity: LocationLatLngEntity,
    private val restaurantRepository: RestaurantRepository
): BaseViewModel() {

    private val _restaurantListLiveData = MutableLiveData<List<RestaurantModel>>()
    val restaurantListLiveData: LiveData<List<RestaurantModel>> = _restaurantListLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        val restaurantList = restaurantRepository.getList(restaurantCategory, locationLatLngEntity)
        _restaurantListLiveData.value = restaurantList.map {
            RestaurantModel(
                id = it.id,
                restaurantInfoId = it.restaurantInfoId,
                restaurantCategory = it.restaurantCategory,
                restaurantTitle = it.restaurantTitle,
                restaurantImageUrl = it.restaurantImageUrl,
                grade = it.grade,
                reviewCount = it.reviewCount,
                deliveryTimeRange = it.deliveryTimeRange,
                deliveryTipRange = it.deliveryTipRange
            )
        }
    }

    fun setLocationLatLng(locationLatLngEntity: LocationLatLngEntity)  {
        this.locationLatLngEntity = locationLatLngEntity
        fetchData()
    }

}