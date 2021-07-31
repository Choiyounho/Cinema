package com.soten.moviereview.data.api

import com.soten.moviereview.domain.model.Review

interface ReviewApi {

    suspend fun getLatestReview(movieId: String): Review?

    suspend fun getAllReviews(movieId: String): List<Review>
}