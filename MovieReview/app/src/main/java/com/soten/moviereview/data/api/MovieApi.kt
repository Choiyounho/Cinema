package com.soten.moviereview.data.api

import com.soten.moviereview.domain.model.Movie

interface MovieApi {

    suspend fun getAllMovies(): List<Movie>

}