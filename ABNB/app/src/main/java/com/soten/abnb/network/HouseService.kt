package com.soten.abnb.network

import com.soten.abnb.dto.HouseDto
import retrofit2.Call
import retrofit2.http.GET

interface HouseService {

    @GET("v3/a5c8bc0f-6150-484f-8852-95eb79ef8cb1")
    fun getHouseList(): Call<HouseDto>
}