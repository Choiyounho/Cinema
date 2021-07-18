package com.soten.shopping.data.entity.product

import java.util.*

// Room Entity
data class ProductEntity(
    val id: Long,
    val createdAt: Date,
    val productName: String,
    val productPrice: Int,
    val productImage: String,
    val productType: String,
    val productIntroductionImage: String
)
