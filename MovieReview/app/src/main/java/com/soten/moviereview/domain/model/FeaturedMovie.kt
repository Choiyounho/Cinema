package com.soten.moviereview.domain.model

data class FeaturedMovie(
    val movie: Movie,
    val latestReview: Review?
)
