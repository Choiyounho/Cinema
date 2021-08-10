package com.soten.fooddelivery.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soten.fooddelivery.data.db.dao.LocationDao
import com.soten.fooddelivery.data.db.dao.RestaurantDao
import com.soten.fooddelivery.data.entity.LocationLatLngEntity
import com.soten.fooddelivery.data.entity.RestaurantEntity

@Database(
    entities = [LocationLatLngEntity::class, RestaurantEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ApplicationDatabase : RoomDatabase() {

    abstract fun LocationDao(): LocationDao
    abstract fun RestaurantDao(): RestaurantDao

    companion object {
        const val DB_NAME = "ApplicationDataBase.db"

    }

}