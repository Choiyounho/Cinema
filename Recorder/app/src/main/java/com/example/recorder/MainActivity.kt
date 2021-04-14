package com.example.recorder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    private val recordingButton: RecordingButton by lazy {
        findViewById(R.id.recordButton)
    }

    private var state = State.BEFORE_RECORDING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    private fun initViews() {
        recordingButton.updateIconWithState(state)
    }
}