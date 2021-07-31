package com.soten.moviereview.domain.usecase

import com.soten.moviereview.data.repository.MovieRepository
import com.soten.moviereview.domain.model.Movie

class GetAllMoviesUseCase(private val movieRepository: MovieRepository) {

    suspend operator fun invoke(): List<Movie> = movieRepository.getAllMovies()

}