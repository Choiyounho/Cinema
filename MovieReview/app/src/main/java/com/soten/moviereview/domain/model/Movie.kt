package com.soten.moviereview.domain.model

import com.google.firebase.firestore.DocumentId

data class Movie(
    @DocumentId
    val id: String? = null,

    @field:JvmField // 필드가 Boolean 일 경우 붙여주는 어노테이션
    val isFeatured: Boolean? = null,

    val title: String? = null,
    val actors: String? = null,
    val country: String? = null,
    val director: String? = null,
    val genre: String? = null,
    val posterUrl: String? = null,
    val rating: String? = null,
    val averageScore: Float? = null,
    val numberOfScore: Int? = null,
    val releaseYear: Int? = null,
    val runtime: Int? = null
)