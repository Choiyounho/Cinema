package com.soten.fooddelivery.data.repository.user

import com.soten.fooddelivery.data.entity.LocationLatLngEntity
import com.soten.fooddelivery.data.entity.RestaurantEntity

interface UserRepository {

    suspend fun getUserLocation(): LocationLatLngEntity?

    suspend fun insertUserLocation(locationLatLngEntity: LocationLatLngEntity)

    suspend fun getUserLikedRestaurant(restaurantTitle: String): RestaurantEntity?

    suspend fun getAllUserLikedRestaurant(): List<RestaurantEntity>

    suspend fun insertUserLikedRestaurant(restaurantEntity: RestaurantEntity)

    suspend fun deleteUserLikedRestaurant(restaurantTitle: String)

    suspend fun deleteAllUserLikedRestaurant()
}