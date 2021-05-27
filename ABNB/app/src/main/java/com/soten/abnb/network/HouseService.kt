package com.soten.abnb.network

import com.soten.abnb.data.HouseDto
import retrofit2.Call
import retrofit2.http.GET

interface HouseService {

    @GET("https://run.mocky.io/v3/f290be71-a45a-4b15-8902-af303233cf86")
    fun getHouseList(): Call<HouseDto>
}