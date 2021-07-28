package com.soten.deliverycheck.data.db

import androidx.room.*
import com.soten.deliverycheck.data.entity.TrackingItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackingItemDao {

    @Query("SELECT * FROM TrackingItem")
    fun allTrackingItems(): Flow<List<TrackingItem>>

    @Query("SELECT * FROM TrackingItem")
    suspend fun getAll(): List<TrackingItem>

    @Insert(onConflict = OnConflictStrategy.IGNORE) // 프라이머리 키 기준 이미 겹치는 부분이 있다면(저장 되어 있다면) 새로 추가하지 않는다.
    suspend fun insert(item: TrackingItem)

    @Delete
    suspend fun delete(item: TrackingItem)

}