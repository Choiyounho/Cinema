package com.soten.youtube.ui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.soten.youtube.R
import com.soten.youtube.databinding.FragmentPlayerBinding
import com.soten.youtube.dto.VideoDto
import com.soten.youtube.network.VideoService
import com.soten.youtube.ui.adapter.VideoAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.abs

class PlayerFragment : Fragment(R.layout.fragment_player) {

    private lateinit var videoAdapter: VideoAdapter
    private var binding: FragmentPlayerBinding? = null
    private var player: SimpleExoPlayer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentPlayerBinding = FragmentPlayerBinding.bind(view)
        binding = fragmentPlayerBinding

        initPlayerRemote(fragmentPlayerBinding)

        initMotionLayoutEvent(fragmentPlayerBinding)
        initRecyclerView(fragmentPlayerBinding)
        initPlayer(fragmentPlayerBinding)
        initControlButton(fragmentPlayerBinding)

        getVideoList()
    }

    private fun initPlayerRemote(fragmentPlayerBinding: FragmentPlayerBinding) {
        fragmentPlayerBinding.playerMotionLayout.isVisible = false

        fragmentPlayerBinding.mainContainerLayout.setOnLongClickListener {
            fragmentPlayerBinding.playerMotionLayout.isVisible = false
            fragmentPlayerBinding.bottomPlayerControlButton.isVisible = false
            fragmentPlayerBinding.playerView.isVisible = false
            fragmentPlayerBinding.bottomTitleTextView.isVisible = false
            player?.release()
            return@setOnLongClickListener true
        }
    }

    // binding을 사용하지 않는 이유 : null 처리를 안해줘도 된다.
    // MainActivity와 PlayerFragment를 이어줘서 같이 모션을 취하게 하는 코드
    private fun initMotionLayoutEvent(fragmentPlayerBinding: FragmentPlayerBinding) {
        fragmentPlayerBinding.playerMotionLayout.setTransitionListener(object :
            MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                binding?.let {
                    (activity as MainActivity).also { mainActivity ->
                        mainActivity.findViewById<MotionLayout>(R.id.mainMotionLayout).progress =
                            abs(progress)
                    }
                }
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {}

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
        })
    }

    private fun initRecyclerView(fragmentPlayerBinding: FragmentPlayerBinding) {
        videoAdapter = VideoAdapter(callback = { url, title ->
            play(url, title)
        })
        fragmentPlayerBinding.fragmentRecyclerView.apply {
            adapter = videoAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initPlayer(fragmentPlayerBinding: FragmentPlayerBinding) {
        context?.let {
            player = SimpleExoPlayer.Builder(it).build()
        }

        fragmentPlayerBinding.playerView.player = player

        binding?.let {
            player?.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)

                    if (isPlaying) {
                        fragmentPlayerBinding.playerMotionLayout.isVisible = true
                        fragmentPlayerBinding.bottomPlayerControlButton.isVisible = true
                        fragmentPlayerBinding.playerView.isVisible = true
                        fragmentPlayerBinding.bottomTitleTextView.isVisible = true
                        it.bottomPlayerControlButton.setImageResource(R.drawable.ic_pause_24)
                    } else {
                        it.bottomPlayerControlButton.setImageResource(R.drawable.ic_play_arrow_24)
                    }
                }
            })
        }
    }

    private fun initControlButton(fragmentPlayerBinding: FragmentPlayerBinding) {
        fragmentPlayerBinding.bottomPlayerControlButton.setOnClickListener {
            val player = this.player ?: return@setOnClickListener

            if (player.isPlaying) {
                player.pause()
            } else {
                player.play()
            }
        }
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
                            videoAdapter.submitList(dto.videos)
                        }
                    }

                    override fun onFailure(call: Call<VideoDto>, t: Throwable) {
                        Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    fun play(url: String, title: String) {
        context?.let {
            val dataSourceFactory = DefaultDataSourceFactory(it)
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(Uri.parse(url)))
            player?.setMediaSource(mediaSource)
            player?.prepare()
            player?.play()
        }

        binding?.let {
            it.playerMotionLayout.transitionToEnd()
            it.bottomTitleTextView.text = title
        }

    }

    override fun onStop() {
        super.onStop()

        player?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
        player?.release()
    }
}