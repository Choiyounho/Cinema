package com.soten.youtube

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.soten.youtube.dto.VideoDto
import com.soten.youtube.network.VideoService
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, PlayerFragment())
            .commit()

        getVideoList()
    }

    private fun getVideoList() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://run.mocky.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(VideoService::class.java).also {
            it.listVideos()
                .enqueue(object : Callback<VideoDto> {
                    override fun onResponse(call: Call<VideoDto>, response: Response<VideoDto>) {
                        if (response.isSuccessful.not()) {
                            Log.d("FFFFFF", "response fail")
                            return
                        }

                        response.body()?.let { dto ->
                            Log.d("TTTTTT", "${dto.videos.size}")

                        }
                    }

                    override fun onFailure(call: Call<VideoDto>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "Fail", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}