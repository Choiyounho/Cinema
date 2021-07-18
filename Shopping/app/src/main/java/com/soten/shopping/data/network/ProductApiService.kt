package com.soten.shopping.data.network

import com.soten.shopping.data.response.ProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApiService {

    @GET("products")
    suspend fun getProducts(): Response<ProductResponse>

    @GET("products/{productId}")
    suspend fun getProduct(@Path("productId") productId: Long): Response<ProductResponse>

}