package com.thedev.mercadolivre.features.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thedev.mercadolivre.core.network.products.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(private val productsRepository: ProductsRepository) :
    ViewModel() {

    private val _state = MutableStateFlow(ProductState())
    val state: StateFlow<ProductState> = _state.asStateFlow()

    fun loadProductDetails(productId: String) {
        viewModelScope.launch {
            val product = runCatching { productsRepository.getProduct(productId) }.getOrNull()
                ?: return@launch _state.update { it.copy(error = "Failed to load product details") }
            _state.update { it.copy(product = product) }
        }
    }
}
