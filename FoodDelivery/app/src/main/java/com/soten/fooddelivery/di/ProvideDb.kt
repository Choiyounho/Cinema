package com.soten.fooddelivery.di

import android.content.Context
import androidx.room.Room
import com.soten.fooddelivery.data.db.ApplicationDatabase

fun provideDb(context: Context): ApplicationDatabase =
    Room.databaseBuilder(context, ApplicationDatabase::class.java, ApplicationDatabase.DB_NAME)
        .build()

fun provideLocationDao(database: ApplicationDatabase) = database.LocationDao()