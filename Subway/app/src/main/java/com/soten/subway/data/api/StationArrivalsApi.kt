package com.soten.subway.data.api

import com.soten.subway.BuildConfig
import com.soten.subway.data.api.response.RealtimeStationArrivals
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface StationArrivalsApi {

    @GET("api/subway/${BuildConfig.SEOUL_API_ACCESS_KEY}/json/realtimeStationArrival/0/100/{stationName}")
    suspend fun getRealtimeStationsArrivals(@Path("stationName") stationName: String): Response<RealtimeStationArrivals>

}