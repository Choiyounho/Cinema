package com.soten.shopping.domain

import com.soten.shopping.data.entity.product.ProductEntity
import com.soten.shopping.data.repository.ProductRepository

internal class GetProductListUseCase(
    private val productRepository: ProductRepository
): UseCase {

    suspend operator fun invoke(): List<ProductEntity> {
        return productRepository.getProductList()
    }
}