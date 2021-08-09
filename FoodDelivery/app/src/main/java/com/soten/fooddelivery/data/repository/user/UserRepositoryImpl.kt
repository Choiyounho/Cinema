package com.soten.fooddelivery.data.repository.user

import com.soten.fooddelivery.data.db.dao.LocationDao
import com.soten.fooddelivery.data.entity.LocationLatLngEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val locationDao: LocationDao,
    private val ioDispatcher: CoroutineDispatcher
) : UserRepository {

    override suspend fun getUserLocation(): LocationLatLngEntity? = withContext(ioDispatcher){
        locationDao.get(-1)
    }

    override suspend fun insertUserLocation(locationLatLngEntity: LocationLatLngEntity) = withContext(ioDispatcher){
        locationDao.insert(locationLatLngEntity)
    }

}