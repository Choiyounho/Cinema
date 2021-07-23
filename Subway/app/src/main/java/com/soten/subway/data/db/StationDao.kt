package com.soten.subway.data.db

import androidx.room.*
import com.soten.subway.data.db.entity.StationEntity
import com.soten.subway.data.db.entity.StationWithSubwaysEntity
import com.soten.subway.data.db.entity.StationsSubwayCrossRefEntity
import com.soten.subway.data.db.entity.SubwayEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface StationDao {

    @Transaction
    @Query("SELECT * FROM StationEntity")
    fun getStationWithSubways(): Flow<List<StationWithSubwaysEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStations(station: List<StationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubways(subways: List<SubwayEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossReferences(reference: List<StationsSubwayCrossRefEntity>)

    @Transaction
    suspend fun insertStationSubways(stationSubways: List<Pair<StationEntity, SubwayEntity>>) {
        insertStations(stationSubways.map { it.first })
        insertSubways(stationSubways.map { it.second })
        insertCrossReferences(
            stationSubways.map { (station, subway) ->
                StationsSubwayCrossRefEntity(
                    station.stationName,
                    subway.subwayId
                )
            }
        )
    }

    @Update
    suspend fun updateStation(station: StationEntity)

}