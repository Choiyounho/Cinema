package com.soten.subway.data.db.entity.mapper

import com.soten.subway.data.db.entity.StationEntity
import com.soten.subway.data.db.entity.StationWithSubwaysEntity
import com.soten.subway.data.db.entity.SubwayEntity
import com.soten.subway.domain.Station
import com.soten.subway.domain.Subway

fun StationWithSubwaysEntity.toStation() =
    Station(
        name = station.stationName,
        isFavorite = station.isFavorited,
        connectedSubways = subways.toSubways()
    )

fun Station.toStationEntity() =
    StationEntity(
        stationName = name,
        isFavorited = isFavorite,
    )


fun List<StationWithSubwaysEntity>.toStations() = map { it.toStation() }

fun List<SubwayEntity>.toSubways(): List<Subway> = map { Subway.findById(it.subwayId) }
