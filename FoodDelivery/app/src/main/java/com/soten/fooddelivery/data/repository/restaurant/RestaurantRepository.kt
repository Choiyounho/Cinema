package com.soten.fooddelivery.data.repository.restaurant

import com.soten.fooddelivery.data.entity.LocationLatLngEntity
import com.soten.fooddelivery.data.entity.RestaurantEntity
import com.soten.fooddelivery.screen.main.home.restaurant.RestaurantCategory

interface RestaurantRepository {

    suspend fun getList(
        restaurantCategory: RestaurantCategory,
        locationLatLngEntity: LocationLatLngEntity
    ): List<RestaurantEntity>

}