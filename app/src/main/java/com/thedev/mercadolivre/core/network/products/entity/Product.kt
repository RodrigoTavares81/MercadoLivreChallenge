package com.thedev.mercadolivre.core.network.products.entity

import com.google.gson.annotations.SerializedName

data class Product(
    val id: String,
    val title: String,
    val seller: Seller,
    val price: Double,
    @SerializedName("currency_id") val currencyId: String,
    @SerializedName("available_quantity") val availableQuantity: Int,
    @SerializedName("sold_quantity") val soldQuantity: Int,
    val thumbnail: String,
    val condition: String,
    val permalink: String,
    val attributes: List<Attribute> = emptyList()
)

data class Attribute(
    val id: String,
    val name: String,
    val value_id: String,
    val value_name: String
)