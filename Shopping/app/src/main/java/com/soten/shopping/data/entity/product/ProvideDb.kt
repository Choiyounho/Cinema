package com.soten.shopping.data.entity.product

import android.content.Context
import androidx.room.Room
import com.soten.shopping.data.db.ProductDatabase

internal fun provideDb(context: Context): ProductDatabase =
    Room.databaseBuilder(context, ProductDatabase::class.java, ProductDatabase.DB_NAME).build()

internal fun provideTodoDao(database: ProductDatabase) = database.productDao()