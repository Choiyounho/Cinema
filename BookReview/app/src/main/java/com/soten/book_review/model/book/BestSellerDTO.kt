package com.soten.book_review.model.book

import com.google.gson.annotations.SerializedName

/**
 * 책의 전체 모델
 */
data class BestSellerDTO(
    @SerializedName("title") val title: String,
    @SerializedName("item") val books: List<Book>,

    ) {
}