package com.soten.subway.repository

import com.soten.subway.data.api.StationApi
import com.soten.subway.data.db.StationDao
import com.soten.subway.data.db.entity.mapper.toStations
import com.soten.subway.data.preference.PreferenceManager
import com.soten.subway.domain.Station
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class StationRepositoryImpl(
    private val stationApi: StationApi,
    private val stationDao: StationDao,
    private val preferenceManager: PreferenceManager,
    private val dispatcher: CoroutineDispatcher
) : StationRepository {

    override val station: Flow<List<Station>> =
        stationDao.getStationWithSubways()
            .distinctUntilChanged() // Observable 하며 과도한 방출을 방지
            .map { it.toStations() }
            .flowOn(dispatcher) // 어떤 스레드에서 데이터 흐름을 사용할 지 (IO 에서 하면 됨)


    override suspend fun refreshStations() {
        val fileUpdatedTimeMillis = stationApi.getStationDataUpdatedTimeMillis()
        val lastDatabaseUpdatedTimeMillis = preferenceManager.getLong(KEY_LAST_DATABASE_UPDATED_TIME_MILLIS)

        // 업데이트된 시점이 존재하지 않거나, 파이어베이스에 업데이트 된 시점이, 지금 내가 업데이트하는 시간보다 더 최신이면 갱신
        if (lastDatabaseUpdatedTimeMillis == null || fileUpdatedTimeMillis > lastDatabaseUpdatedTimeMillis) {
            stationDao.insertStationSubways(stationApi.getStationSubways())
            preferenceManager.putLong(KEY_LAST_DATABASE_UPDATED_TIME_MILLIS, fileUpdatedTimeMillis)
        }
    }

    companion object {
        private const val KEY_LAST_DATABASE_UPDATED_TIME_MILLIS = "KEY_LAST_DATABASE_UPDATED_TIME_MILLIS"
    }
}