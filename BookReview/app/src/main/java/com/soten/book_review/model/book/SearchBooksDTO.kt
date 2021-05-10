package com.soten.book_review.model.book

import com.google.gson.annotations.SerializedName

data class SearchBooksDTO(
    @SerializedName("title") val title: String,
    @SerializedName("item") val books: List<Book>
)
