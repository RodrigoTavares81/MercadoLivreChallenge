package com.thedev.mercadolivre.core.network.products.entity

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("site_id") val siteId: String,
    val query: String,
    val paging: Paging,
    val results: List<Product>
)

