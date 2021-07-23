package com.soten.subway.data.db.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class StationWithSubwaysEntity(
    @Embedded val station: StationEntity,
    @Relation(
        parentColumn = "stationName",
        entityColumn = "subwayId",
        associateBy = Junction(StationsSubwayCrossRefEntity::class)
    )
    val subways: List<SubwayEntity>
)
