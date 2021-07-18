package com.soten.shopping.domain

import com.soten.shopping.data.entity.product.ProductEntity
import com.soten.shopping.data.repository.ProductRepository

internal class OrderProductItemUseCase(
    private val productRepository: ProductRepository
): UseCase {

    suspend operator fun invoke(productEntity: ProductEntity): Long {
        return productRepository.insertProductItem(productEntity)
    }
}