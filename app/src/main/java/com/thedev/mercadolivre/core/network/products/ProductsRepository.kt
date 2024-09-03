package com.thedev.mercadolivre.core.network.products

import com.thedev.mercadolivre.core.network.products.entity.Product

interface ProductsRepository {

    suspend fun searchProducts(query: String, amount: Int, offset: Int): List<Product>

    suspend fun getProduct(id: String): Product
}