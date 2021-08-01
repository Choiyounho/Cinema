package com.soten.moviereview.domain.usecase

import com.soten.moviereview.data.repository.ReviewRepository
import com.soten.moviereview.data.repository.UserRepository
import com.soten.moviereview.domain.model.MovieReviews
import com.soten.moviereview.domain.model.Review
import com.soten.moviereview.domain.model.User

class GetAllMovieReviewsUseCase(
    private val userRepository: UserRepository,
    private val reviewRepository: ReviewRepository
) {

    suspend operator fun invoke(movieId: String): MovieReviews {
        val reviews = reviewRepository.getAllReviews(movieId)
        val user = userRepository.getUser()

        if (user == null) {
            userRepository.saveUser(User())

            return MovieReviews(null, reviews)
        }

        return MovieReviews(
            reviews.find { it.userId == user.id },
            reviews.filter { it.userId != user.id }
        )
    }
}