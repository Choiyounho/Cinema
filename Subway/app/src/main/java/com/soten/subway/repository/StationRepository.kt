package com.soten.subway.repository

import com.soten.subway.domain.Station
import kotlinx.coroutines.flow.Flow

interface StationRepository {

    val station: Flow<List<Station>>

    suspend fun refreshStations()

}