package com.soten.moviereview.domain.usecase

import com.soten.moviereview.data.repository.ReviewRepository
import com.soten.moviereview.domain.model.Review

class DeleteReviewUseCase(
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(review: Review) =
        reviewRepository.removeReview(review)
}
