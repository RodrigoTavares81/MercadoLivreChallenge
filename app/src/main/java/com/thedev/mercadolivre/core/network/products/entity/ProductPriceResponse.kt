package com.thedev.mercadolivre.core.network.products.entity

data class ProductPriceResponse(
    val prices: List<Price>
) {
    fun getBrazilianPrice(): Price? = prices.find { it.currency_id == "BRL" }
}