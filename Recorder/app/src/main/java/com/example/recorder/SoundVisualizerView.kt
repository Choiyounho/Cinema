package com.example.recorder

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import java.util.*

class SoundVisualizerView(
    context: Context,
    attr: AttributeSet? = null
) : View(context, attr) {

    var onRequestCurrentAmplitude: (() -> Int)? = null

    private val amplitudePaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {  // paint : 객체를 어떻게 그릴것인지 정하는 것
            color = context.getColor(R.color.purple_500)
            strokeWidth = LINE_WIDTH
            strokeCap = Paint.Cap.ROUND
        }
    private var drawingWidth: Int = 0
    private var drawingHeight: Int = 0
    private var drawingAmplitudes: List<Int> = emptyList()
    private var isReplaying: Boolean = false
    private var rePlayingPosition: Int = 0

    private val visualizeRepeatAction: Runnable = object : Runnable {
        override fun run() {
            if (!isReplaying) {
                val currentAmplitude = onRequestCurrentAmplitude?.invoke() ?: 0
                drawingAmplitudes = listOf(currentAmplitude) + drawingAmplitudes
            } else {
                rePlayingPosition++
            }

            invalidate()

            handler?.postDelayed(this, ACTION_INTERVAL)
        }

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        drawingWidth = w
        drawingHeight = h
    }

    override fun onDraw(canvas: Canvas?) { // canvas : 그림을 그릴 객체
        super.onDraw(canvas)

        canvas ?: return

        val centerY = drawingHeight / 2f
        var offsetX = drawingWidth.toFloat()

        drawingAmplitudes
            .let { amplitudes ->
                if (isReplaying) {
                    amplitudes.takeLast(rePlayingPosition)
                } else {
                    amplitudes
                }
            }.forEach { amplitude ->
                val lineLength = amplitude / MAX_AMPLITUDE * drawingHeight * 0.8f

                offsetX -= LINE_SPACE
                if (offsetX < 0) return@forEach

                canvas.drawLine(
                    offsetX,
                    centerY - lineLength / 2f,
                    offsetX,
                    centerY + lineLength / 2f,
                    amplitudePaint
                )
            }
    }

    fun startVisualizing(isReplaying: Boolean) {
        this.isReplaying = isReplaying
        handler?.post(visualizeRepeatAction)
    }

    fun stopVisualizing() {
        rePlayingPosition = 0
        handler?.removeCallbacks(visualizeRepeatAction)
    }

    fun clearVisualization() {
        drawingAmplitudes = emptyList()
        invalidate()
    }

    companion object {
        private const val LINE_WIDTH = 10f
        private const val LINE_SPACE = 15f
        private const val MAX_AMPLITUDE = Short.MAX_VALUE.toFloat()
        private const val ACTION_INTERVAL = 20L
    }

}