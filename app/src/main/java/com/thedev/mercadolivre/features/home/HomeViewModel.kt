package com.thedev.mercadolivre.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thedev.mercadolivre.core.network.products.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val productsRepository: ProductsRepository) :
    ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private var searchJob: Job? = null

    fun onEvent(action: HomeActions) {
        when (action) {
            is HomeActions.ChangeQuery -> onQueryChanged(action.query)
            is HomeActions.ScrollToEnd -> onScrollToEnd()
        }
    }

    private fun onScrollToEnd() {
        searchJob?.cancel()
        _state.update { it.copy(isLoadingMore = true) }
        searchJob = viewModelScope.launch {
            performSearchMore()
        }
    }

    private suspend fun performSearchMore() {
        val results = runCatching {
            val query = _state.value.searchQuery
            val offset = _state.value.searchResults.size
            productsRepository.searchProducts(query, 20, offset)
        }.getOrElse { error ->
            _state.update {
                it.copy(
                    error = "Failed to load more products ${error.message}",
                    isLoadingMore = false
                )
            }
            return
        }
        _state.update {
            it.copy(
                searchResults = it.searchResults + results,
                isLoadingMore = false
            )
        }
    }

    private fun onQueryChanged(query: String) {
        searchJob?.cancel()
        _state.update { it.copy(searchQuery = query, isLoading = true) }
        searchJob = viewModelScope.launch {
            delay(500)
            performNewSearch(query)
        }
    }

    private suspend fun performNewSearch(query: String) {
        val results = runCatching {
            productsRepository.searchProducts(query, 20, 0)
        }.getOrElse { error ->
            return _state.update {
                it.copy(
                    error = "Failed to search products ${error.message}",
                    searchResults = emptyList(),
                    isLoading = false
                )
            }
        }
        _state.update { it.copy(searchResults = results, isLoading = false) }
    }
}