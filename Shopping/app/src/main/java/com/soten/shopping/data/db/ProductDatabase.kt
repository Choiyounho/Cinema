package com.soten.shopping.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.soten.shopping.data.db.dao.ProductDao
import com.soten.shopping.data.entity.product.ProductEntity
import com.soten.shopping.utility.DateConverter

@Database(
    entities = [ProductEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class) // date로 온 프로퍼티를 온전하게 관리하기 위해
abstract class ProductDatabase: RoomDatabase() {

    companion object {
        const val DB_NAME = "ProductDataBase.db"
    }

    abstract fun productDao(): ProductDao
}