package com.soten.deliverycheck.extensions

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*

// Context
fun Context.color(@ColorRes colorResId: Int): Int = ContextCompat.getColor(this, colorResId)


// Date
private val dateFormat = SimpleDateFormat("MM.dd", Locale.KOREA)

fun Date.toReadableDateString(): String = dateFormat.format(this)

// TextView
fun TextView.setTextColorRes(@ColorRes colorResId: Int) {
    setTextColor(color(colorResId))
}

// View
fun View.color(@ColorRes colorResId: Int) = context.color(colorResId)