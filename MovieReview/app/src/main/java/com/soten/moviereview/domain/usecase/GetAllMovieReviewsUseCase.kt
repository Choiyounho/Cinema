package com.soten.moviereview.domain.usecase

import com.soten.moviereview.data.repository.ReviewRepository
import com.soten.moviereview.domain.model.Review

class GetAllMovieReviewsUseCase(private val reviewRepository: ReviewRepository) {

    suspend operator fun invoke(movieId: String): List<Review> =
        reviewRepository.getAllReviews(movieId)

}