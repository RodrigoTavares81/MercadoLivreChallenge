package com.thedev.mercadolivre.core.network.products

import com.thedev.mercadolivre.core.network.products.entity.Product
import com.thedev.mercadolivre.core.network.products.entity.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductsApi {
    @GET("sites/MLA/search")
    suspend fun searchProducts(
        @Query("q") query: String,
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int = 0
    ): SearchResponse

    @GET("items/{item_id}")
    suspend fun getProduct(@Path("item_id") itemId: String): Product
}