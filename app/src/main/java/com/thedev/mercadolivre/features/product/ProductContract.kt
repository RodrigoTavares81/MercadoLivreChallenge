package com.thedev.mercadolivre.features.product

import com.thedev.mercadolivre.core.network.products.entity.Product

sealed class ProductActions {
    data class UpdateSearchQuery(val query: String) : ProductActions()
    object PerformSearch : ProductActions()
}

data class ProductState(
    val product: Product? = null,
    val error: String? = null,
)