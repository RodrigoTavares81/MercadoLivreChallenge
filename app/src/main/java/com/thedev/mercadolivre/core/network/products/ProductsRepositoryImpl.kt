package com.thedev.mercadolivre.core.network.products

import com.thedev.mercadolivre.core.network.products.entity.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(private val api: ProductsApi) :
    ProductsRepository {

    override suspend fun searchProducts(query: String, amount: Int, offset: Int): List<Product> =
        withContext(Dispatchers.IO) {
            val response = api.searchProducts(query, amount, offset)
            response.results
        }

    override suspend fun getProduct(id: String): Product = withContext(Dispatchers.IO) {
        api.getProduct(id)
    }
}
