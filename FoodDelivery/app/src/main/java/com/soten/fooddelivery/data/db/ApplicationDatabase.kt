package com.soten.fooddelivery.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soten.fooddelivery.data.db.dao.LocationDao
import com.soten.fooddelivery.data.entity.LocationLatLngEntity

@Database(
    entities = [LocationLatLngEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ApplicationDatabase : RoomDatabase() {

    abstract fun LocationDao(): LocationDao

    companion object {
        const val DB_NAME = "ApplicationDataBase.db"

    }

}