package com.soten.fooddelivery.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soten.fooddelivery.data.db.dao.FoodMenuBasketDao
import com.soten.fooddelivery.data.db.dao.LocationDao
import com.soten.fooddelivery.data.db.dao.RestaurantDao
import com.soten.fooddelivery.data.entity.LocationLatLngEntity
import com.soten.fooddelivery.data.entity.RestaurantEntity
import com.soten.fooddelivery.data.entity.RestaurantFoodEntity

@Database(
    entities = [LocationLatLngEntity::class, RestaurantEntity::class, RestaurantFoodEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ApplicationDatabase : RoomDatabase() {

    abstract fun LocationDao(): LocationDao
    abstract fun RestaurantDao(): RestaurantDao
    abstract fun FoodMenuBasketDao(): FoodMenuBasketDao

    companion object {
        const val DB_NAME = "ApplicationDataBase.db"

    }

}