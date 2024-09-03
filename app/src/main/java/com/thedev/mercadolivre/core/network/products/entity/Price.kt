package com.thedev.mercadolivre.core.network.products.entity

data class Price(
    val amount: Int,
    val conditions: Conditions,
    val currency_id: String
)