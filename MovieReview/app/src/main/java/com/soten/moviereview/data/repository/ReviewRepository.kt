package com.soten.moviereview.data.repository

import com.soten.moviereview.domain.model.Review

interface ReviewRepository {

    suspend fun getLatestReview(movieId: String): Review?

    suspend fun getAllReviews(movieId: String): List<Review>

    suspend fun getAllUserReviews(userId: String): List<Review>

    suspend fun addReview(review: Review): Review

    suspend fun removeReview(review: Review)

}