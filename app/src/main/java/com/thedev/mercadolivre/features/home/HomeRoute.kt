package com.thedev.mercadolivre.features.home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.thedev.mercadolivre.core.network.products.entity.Product

@Composable
fun HomeRoute(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    HomeScreen(
        state = viewModel.state.collectAsState().value,
        onEvent = { action -> viewModel.onEvent(action) },
        navigateToProductDetail = { productId -> navController.navigate("productDetail/$productId") }
    )
}

@Composable
fun HomeScreen(
    state: HomeState,
    onEvent: (HomeActions) -> Unit,
    navigateToProductDetail: (String) -> Unit
) {
    Scaffold(
        topBar = { HomeTopBar() },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                SearchField(
                    searchQuery = state.searchQuery,
                    onQueryChange = { onEvent(HomeActions.ChangeQuery(it)) }
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (state.isLoading) {
                    LoadingIndicator()
                } else {
                    ProductList(
                        products = state.searchResults,
                        isLoadingMore = state.isLoadingMore,
                        onEndReached = { onEvent(HomeActions.ScrollToEnd) },
                        onProductClick = navigateToProductDetail
                    )
                }

                state.error?.let { error ->
                    ErrorSnackbar(
                        error = error,
                        onRetry = { /* Handle retry logic */ }
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar() {
    TopAppBar(
        title = { Text("Search Products") }
    )
}

@Composable
fun SearchField(searchQuery: String, onQueryChange: (String) -> Unit) {
    TextField(
        value = searchQuery,
        onValueChange = onQueryChange,
        label = { Text("Search") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun LoadingIndicator() {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ProductList(
    products: List<Product>,
    isLoadingMore: Boolean,
    onEndReached: () -> Unit,
    onProductClick: (String) -> Unit
) {
    LazyColumn {
        itemsIndexed(products) { index, product ->
            if (index == products.size - 8) {
                LaunchedEffect(Unit) {
                    onEndReached()
                }
            }
            ProductListItem(
                product = product,
                onClick = { onProductClick(product.id) },
            )
            val isAtEndOfList = index == products.size - 1
            if (isAtEndOfList && isLoadingMore) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun ErrorSnackbar(error: String, onRetry: () -> Unit) {
    Snackbar(
        modifier = Modifier.padding(8.dp),
        action = {
            TextButton(onClick = onRetry) {
                Text("Retry")
            }
        }
    ) {
        Text(text = error)
    }
}

@Composable
fun ProductListItem(product: Product, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        AsyncImage(
            model = product.thumbnail,
            contentDescription = product.title,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.align(Alignment.CenterVertically)) {
            Text(
                text = product.title,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$${product.price} ${product.currencyId}",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
        }
    }
}