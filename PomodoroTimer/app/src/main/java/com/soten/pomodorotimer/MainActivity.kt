package com.soten.pomodorotimer

import android.annotation.SuppressLint
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private val remainMinutesTextView: TextView by lazy {
        findViewById(R.id.remainMinutesTextView)
    }

    private val remainSecondsTextView: TextView by lazy {
        findViewById(R.id.remainSecondsTextView)
    }

    private val seekBar: SeekBar by lazy {
        findViewById(R.id.seekBar)
    }

    private val soundPool: SoundPool = SoundPool.Builder().build()

    private var currentCountDownTimer: CountDownTimer? = null
    private var tickingSoundId: Int? = null
    private var bellSoundId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()
        initSounds()
    }

    override fun onResume() {
        super.onResume()

        soundPool.autoResume()
    }

    override fun onPause() {
        super.onPause()

        soundPool.autoPause()
    }

    override fun onDestroy() {
        super.onDestroy()

        soundPool.release()
    }

    private fun bindViews() {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    updateRemainTime(progress * TO_SEC * TO_MIN)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                stopCountDown()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar ?: return

                if (seekBar.progress == INIT_SEEK_BAR) {
                    stopCountDown()
                } else {
                    startCountDown()
                }
            }
        })
    }

    private fun initSounds() {
        tickingSoundId = soundPool.load(this, R.raw.timer_ticking, PRIORITY_ONE)
        bellSoundId = soundPool.load(this, R.raw.timer_bell, PRIORITY_ONE)
    }

    private fun createCountDownTimer(initialMillis: Long) =
        object : CountDownTimer(initialMillis, TO_MIN) {
            override fun onTick(millisUntilFinished: Long) {
                updateRemainTime(millisUntilFinished)
                updateSeekBar(millisUntilFinished)
            }

            override fun onFinish() {
                completeCountDown()
            }
        }

    private fun startCountDown() {
        currentCountDownTimer = createCountDownTimer(seekBar.progress * TO_SEC * TO_MIN)
        currentCountDownTimer?.start()

        tickingSoundId?.let {
            soundPool.play(it, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY_ZERO, LOOP, RATE)
        }
    }

    private fun stopCountDown() {
        currentCountDownTimer?.cancel()
        currentCountDownTimer = null
        soundPool.autoPause()
    }

    private fun completeCountDown() {
        updateRemainTime(REMAIN_MILLIS)
        updateSeekBar(REMAIN_MILLIS)

        soundPool.autoPause()

        bellSoundId?.let {
            soundPool.play(it, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY_ZERO, LOOP, RATE)
            Thread.sleep(FINISH_SOUND)
            soundPool.release()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateRemainTime(remainMillis: Long) {
        val remainSeconds = remainMillis / TO_MIN

        remainMinutesTextView.text = FORMAT_MINUTES.format(remainSeconds / TO_SEC)
        remainSecondsTextView.text = FORMAT_SECONDS.format(remainSeconds % TO_SEC)
    }

    private fun updateSeekBar(remainMillis: Long) {
        seekBar.progress = (remainMillis / TO_MIN / TO_SEC).toInt()
    }

    companion object {
        private const val FORMAT_MINUTES = "%02d'"
        private const val FORMAT_SECONDS = "%02d"
        private const val INIT_SEEK_BAR = 0
        private const val REMAIN_MILLIS = 0L
        private const val TO_SEC = 60
        private const val TO_MIN = 1000L
        private const val FINISH_SOUND = 3500L
        private const val LEFT_VOLUME = 1F
        private const val RIGHT_VOLUME = 1F
        private const val LOOP = -1
        private const val RATE = 1F
        private const val PRIORITY_ZERO = 0
        private const val PRIORITY_ONE = 1
    }

}