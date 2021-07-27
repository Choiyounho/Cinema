package com.soten.deliverycheck.data.api

import com.soten.deliverycheck.BuildConfig
import com.soten.deliverycheck.data.entity.ShippingCompanies
import com.soten.deliverycheck.data.entity.TrackingInformation
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SweetTrackerApi {

    @GET("api/v1/trackingInfo?t_key=${BuildConfig.SMART_TRACKER_API_KEY}")
    suspend fun getTrackingInformation(
        @Query("t_code") companyCode: String,
        @Query("t_invoice") invoice: String
    ): Response<TrackingInformation>

    @GET("api/v1/companylist?t_key=${BuildConfig.SMART_TRACKER_API_KEY}")
    suspend fun getShippingCompanies(): Response<ShippingCompanies>

}