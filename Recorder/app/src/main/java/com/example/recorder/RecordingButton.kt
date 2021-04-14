package com.example.recorder

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton
import com.example.recorder.State.*

class RecordingButton(
    context: Context,
    attrs: AttributeSet
): AppCompatImageButton(context, attrs) {

    init {
        setBackgroundResource(R.drawable.shape_oval_button)
    }

    fun updateIconWithState(state: State) {
        when (state) {
            BEFORE_RECORDING -> setImageResource(R.drawable.ic_record)
            AFTER_RECODING -> setImageResource(R.drawable.ic_play)
            ON_RECORDING -> setImageResource(R.drawable.ic_stop)
            ON_PLAYING -> setImageResource(R.drawable.ic_stop)
        }
    }

}