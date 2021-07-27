package com.soten.deliverycheck.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.soten.deliverycheck.entity.TrackingItem

@Dao
interface TrackingItemDao {

    @Query("SELECT * FROM TrackingItem")
    suspend fun getAll(): List<TrackingItem>

    @Insert(onConflict = OnConflictStrategy.IGNORE) // 프라이머리 키 기준 이미 겹치는 부분이 있다면(저장 되어 있다면) 새로 추가하지 않는다.
    suspend fun insert(item: TrackingItem)

}