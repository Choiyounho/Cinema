package com.soten.moviereview.extensions

import android.content.Context
import android.view.View
import androidx.annotation.Px
import java.text.DecimalFormat

// Context
@Px
fun Context.dip(dipValue: Float) = (dipValue * resources.displayMetrics.density).toInt()

// View
@Px
fun View.dip(dipValue: Float) = context.dip(dipValue)

fun View.toVisible() {
    visibility = View.VISIBLE
}

fun View.toGone() {
    visibility = View.GONE
}

// Float
fun Float.toDecimalFormatString(format: String): String = DecimalFormat(format).format(this)

// Int
fun Int.toAbbreviatedString(): String = when (this) {
    in 0..1_000 -> {
        this.toString()
    }
    in 1_000..1_000_000 -> {
        "${(this / 1_000f).toDecimalFormatString("#.#")}K"
    }
    else -> {
        "${(this / 1_000_000f).toDecimalFormatString("#.#")}M"
    }
}