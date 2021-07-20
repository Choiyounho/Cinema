package com.soten.camerax.extexsions

import android.content.Context
import com.soten.camerax.R
import java.io.File

internal fun Context.getAppCachePictureDirectory() : File {
    val mediaDir = filesDir?.let {
        File(it, getString(R.string.app_name)).apply { mkdirs() }
    }
    return if (mediaDir != null && mediaDir.exists()) {
        mediaDir
    } else {
        filesDir
    }
}