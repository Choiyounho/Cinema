package com.soten.subway.presenter.stations

import com.soten.subway.domain.Station
import com.soten.subway.repository.StationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/*
* MutableStateFlow 는 현재 상태와 새로운 상태 업데이트를 옵져빙하는 곳에 보내는 식별 가능한 상태 폴더 흐름이다.
*
* */

class StationsPresenter(
    private val view: StationContract.View,
    private val stationRepository: StationRepository
) : StationContract.Presenter {

    override val scope: CoroutineScope = MainScope()

    // 검색 내용은 가지고 있음
    private val queryString: MutableStateFlow<String> = MutableStateFlow("")
    private val stations: MutableStateFlow<List<Station>> = MutableStateFlow(emptyList())

    init {
        observeStations()
    }

    override fun onViewCreated() {
        scope.launch {
            view.showStations(stations.value)
            stationRepository.refreshStations()
        }
    }

    override fun filterStations(query: String) { // 검색한 것과 관계없으면 제거
        scope.launch {
            queryString.emit(query)
        }
    }

    override fun onDestroyView() {}

    private fun observeStations() {
        stationRepository
            .stations // 전체 역
            .combine(queryString) { stations, query -> // 검색 내용이 있으면 필터링, 없으면 전체
                if (query.isBlank()) {
                    stations
                } else {
                    stations.filter { it.name.contains(query) }
                }
            }
            .onStart { view.showLoadingIndicator() } // stations 플로우를 구독했을 때, 인디케이터를 보여줌
            .onEach {
                // 새로운 값이 들어올 때마다 수행
                if (it.isNotEmpty()) {
                    view.hideLoadingIndicator() // 값이 로딩이 된다면 인디케이터 제거
                }
                stations.value = it // MutableState 에 값을 주입
                view.showStations(it) // 화면에 보여줌
            }
            .catch {
                it.printStackTrace()
                view.hideLoadingIndicator()
            }
            .launchIn(scope) // 스코프 내에서 옵져빙, 스코프가 종료되면 이 로직도 종료
    }

    override fun toggleStationFavorite(station: Station) {
        scope.launch {
            stationRepository.updateStation(station.copy(isFavorited = !station.isFavorited))
        }
    }
}