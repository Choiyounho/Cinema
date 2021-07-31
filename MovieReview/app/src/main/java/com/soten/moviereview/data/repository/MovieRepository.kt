package com.soten.moviereview.data.repository

import com.soten.moviereview.domain.model.Movie

interface MovieRepository {

    suspend fun getAllMovies(): List<Movie>

}