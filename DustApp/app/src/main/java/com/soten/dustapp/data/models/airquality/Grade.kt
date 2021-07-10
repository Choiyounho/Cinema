package com.soten.dustapp.data.models.airquality

import androidx.annotation.ColorRes
import com.google.gson.annotations.SerializedName
import com.soten.dustapp.R

enum class Grade(
    val label: String,
    val emoji: String,
    @ColorRes val colorResId: Int // @ColorRes 색이 아닌 값을 넣으면 경고 표시
) {

    @SerializedName("1")
    GOOD("좋음", "😀", R.color.blue),

    @SerializedName("2")
    NORMAL("보통", "🙂", R.color.green),

    @SerializedName("3")
    BAD("나쁨", "😥", R.color.yellow),

    @SerializedName("4")
    AWFUL("매우나쁨", "😭", R.color.red),

    UNKNOWN("미측정", "🦓", R.color.gray);

    override fun toString(): String {
        return "$label $emoji"
    }
}