package com.soten.musicplayer

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.soten.musicplayer.databinding.FragmentPlayerBinding
import com.soten.musicplayer.service.MusicDto
import com.soten.musicplayer.service.MusicModel
import com.soten.musicplayer.service.MusicService
import com.soten.musicplayer.service.mapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class PlayerFragment : Fragment(R.layout.fragment_player) {

    private var model = PlayerModel()
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private var player: SimpleExoPlayer? = null
    private lateinit var playListAdapter: PlaylistAdapter

    private val updateSeekRunnable = Runnable {
        updateSeek()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentPlayerBinding = FragmentPlayerBinding.bind(view)
        _binding = fragmentPlayerBinding

        // ExoPlayer 사용하기 위한 초기화
        initPlayView()
        initPlaylistButton()
        // 음악을 조작하기 위한 버튼 초기화
        initPlayControlButtons()
        initSeekbar()
        initRecyclerView()

        getVideoListFromServer()
    }

    private fun initSeekbar() {
        binding.playerSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                player?.seekTo((seekBar.progress * 1000).toLong())
            }
        })

        binding.playlistSeekBar.setOnTouchListener { _, _ ->
            false
        }
    }

    private fun initPlayView() {
        context?.let {
            player = SimpleExoPlayer.Builder(it).build()
        }

        binding.playerView.player = player

        binding.run {
            player?.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)

                    if (isPlaying) {
                        binding.playerControlImageView.setImageResource(R.drawable.ic_baseline_pause_48)
                    } else {
                        binding.playerControlImageView.setImageResource(R.drawable.ic_baseline_play_arrow_48)
                    }
                }

                override fun onPlaybackStateChanged(state: Int) {
                    super.onPlaybackStateChanged(state)

                    updateSeek()
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)

                    val newIndex = mediaItem?.mediaId ?: return
                    model.currentPosition = newIndex.toInt()
                    updatePlayerView(model.currentMusicModel())
                    playListAdapter.submitList(model.getAdapterModels())
                }
            })
        }
    }

    private fun updateSeek() {
        val player = this.player ?: return

        val duration = if (player.duration >= 0) player.duration else 0
        val position = player.currentPosition

        updateSeekUi(duration, position)

        val state = player.playbackState

        view?.removeCallbacks(updateSeekRunnable)
        if (state != Player.STATE_IDLE && state != Player.STATE_ENDED) {
            view?.postDelayed(updateSeekRunnable, 1000)
        }
    }

    private fun updateSeekUi(duration: Long, position: Long) {
        binding.playlistSeekBar.max = (duration / 1000).toInt()
        binding.playlistSeekBar.progress = (position / 1000).toInt()

        binding.playerSeekBar.max = (duration / 1000).toInt()
        binding.playerSeekBar.progress = (position / 1000).toInt()

        binding.playTimeTextView.text = String.format(
            "%d:%02d",
            TimeUnit.MINUTES.convert(position, TimeUnit.MICROSECONDS),
            (position / 1000) % 60
        )
        binding.totalTimeTextView.text = String.format(
            "%d:%02d",
            TimeUnit.MINUTES.convert(position, TimeUnit.MICROSECONDS),
            (duration / 1000) % 60
        )
    }

    private fun updatePlayerView(currentMusicModel: MusicModel?) {
        currentMusicModel ?: return

        binding.trackTextView.text = currentMusicModel.track
        binding.artistTextView.text = currentMusicModel.artist
        Glide.with(binding.coverImageView.context)
            .load(currentMusicModel.coverUrl)
            .into(binding.coverImageView)
    }

    private fun initPlaylistButton() {
        binding.playlistImageView.setOnClickListener {
            if (model.currentPosition == -1) return@setOnClickListener

            binding.playerViewGroup.isVisible = model.isWatchingPlayListView
            binding.playlistViewGroup.isVisible = model.isWatchingPlayListView.not()

            model.isWatchingPlayListView = !model.isWatchingPlayListView
        }
    }

    private fun initPlayControlButtons() {
        binding.playerControlImageView.setOnClickListener {
            val player = this.player ?: return@setOnClickListener

            if (player.isPlaying) player.pause() else player.play()
        }

        binding.skipNextImageView.setOnClickListener {
            val nextMusic = model.nextMusic() ?: return@setOnClickListener
            playMusic(nextMusic)
        }

        binding.skipPrevImageView.setOnClickListener {
            val prevMusic = model.prevMusic() ?: return@setOnClickListener
            playMusic(prevMusic)
        }
    }

    private fun initRecyclerView() {
        playListAdapter = PlaylistAdapter {
            playMusic(it)
        }

        binding.playlistRecyclerView.apply {
            adapter = playListAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun getVideoListFromServer() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://run.mocky.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(MusicService::class.java)
            .also {
                it.listMusics()
                    .enqueue(object : Callback<MusicDto> {
                        override fun onResponse(
                            call: Call<MusicDto>,
                            response: Response<MusicDto>
                        ) {
                            Log.d("PlayerFragment", "${response.body()}")

                            response.body()?.let { musicDto ->

                                model = musicDto.mapper()

                                setMusicList(model.getAdapterModels())
                                playListAdapter.submitList(model.getAdapterModels())
                            }
                        }

                        override fun onFailure(call: Call<MusicDto>, t: Throwable) {
                            Toast.makeText(context, "실패", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
    }

    private fun setMusicList(modelList: List<MusicModel>) {
        context?.let {
            player?.addMediaItems(modelList.map { musicModel ->
                MediaItem.Builder()
                    .setMediaId(musicModel.id.toString())
                    .setUri(musicModel.streamUrl)
                    .build()
            })

            player?.prepare()
        }
    }

    private fun playMusic(musicModel: MusicModel) {
        model.updateCurrentPosition(musicModel)
        // 어떤 음악이 어디서 부터 시작될 것인지
        player?.seekTo(model.currentPosition, 0)
        player?.play()
    }


    override fun onStop() {
        super.onStop()

        player?.release()
        view?.removeCallbacks(updateSeekRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()

        player?.release()
        view?.removeCallbacks(updateSeekRunnable)
    }

    companion object {
        fun newInstance(): PlayerFragment {
            return PlayerFragment()
        }
    }

}