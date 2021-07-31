package com.soten.moviereview.domain.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Review(
    @DocumentId
    val id: String? = null,

    @ServerTimestamp // 자동으로 시간을 주입
    val createdAt: Date? = null,

    val userId: String? = null,
    val movieId: String? = null,
    val content: String? = null,
    val score: Float? = null
)