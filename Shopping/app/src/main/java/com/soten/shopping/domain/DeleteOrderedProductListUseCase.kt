package com.soten.shopping.domain

import com.soten.shopping.data.repository.ProductRepository

internal class DeleteOrderedProductListUseCase(
    private val productRepository: ProductRepository
) : UseCase {

    suspend operator fun invoke() {
        return productRepository.deleteAll()
    }
}