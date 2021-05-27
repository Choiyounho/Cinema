package com.soten.youtube.network

import com.soten.youtube.dto.VideoDto
import retrofit2.Call
import retrofit2.http.GET

interface VideoService {

    @GET("v3/8fcdae35-bac5-4d06-abe0-c1a572aa0d5e")
    fun listVideos(): Call<VideoDto>

}