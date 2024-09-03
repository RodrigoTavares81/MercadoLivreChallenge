package com.thedev.mercadolivre.features.home

import com.thedev.mercadolivre.core.network.products.entity.Product

sealed class HomeActions {
    data class ChangeQuery(val query: String) : HomeActions()
    object ScrollToEnd : HomeActions()
}

data class HomeState(
    val searchQuery: String = "",
    val searchResults: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null
)