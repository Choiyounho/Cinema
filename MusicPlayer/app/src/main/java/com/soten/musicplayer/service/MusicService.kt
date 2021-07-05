package com.soten.musicplayer.service

import retrofit2.Call
import retrofit2.http.GET

interface MusicService {

    @GET("/v3/342582f6-902f-430b-aeeb-c12a522a6400")
    fun listMusics(): Call<MusicDto>
}