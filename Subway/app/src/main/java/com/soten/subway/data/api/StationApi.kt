package com.soten.subway.data.api

import com.soten.subway.data.db.entity.StationEntity
import com.soten.subway.data.db.entity.SubwayEntity

interface StationApi {

    suspend fun getStationDataUpdatedTimeMillis(): Long // 최종 업데이트 시간

    suspend fun getStationSubways(): List<Pair<StationEntity, SubwayEntity>>
}