package com.soten.book_review.api

import com.soten.book_review.model.BestSellerDTO
import com.soten.book_review.model.SearchBookDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {

    @GET("/api/search.api?output=json")
    fun getBestByName(
        @Query("key") apiKey : String,
        @Query("query") keyword: String,
    ): Call<SearchBookDTO>

    @GET("/api/bestSeller.api?output=json&categoryId=100")
    fun getBestSellerBooks(
        @Query("key") apiKey: String
    ): Call<BestSellerDTO>

}