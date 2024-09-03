package com.thedev.mercadolivre.features.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.thedev.mercadolivre.core.network.products.entity.Attribute

@Composable
fun ProductRoute(
    productId: String,
    viewModel: ProductViewModel = hiltViewModel()
) {
    viewModel.loadProductDetails(productId)
    ProductScreen(state = viewModel.state.collectAsState().value)
}

@Composable
fun ProductScreen(
    state: ProductState,
) {
    state.product?.let { product ->
        Column(modifier = Modifier.padding(16.dp)) {
            ProductImage(imageUrl = product.thumbnail)
            Spacer(modifier = Modifier.height(16.dp))
            ProductTitle(title = product.title)
            ProductPrice(price = product.price, currencyId = product.currencyId)
            Spacer(modifier = Modifier.height(8.dp))
            ProductCondition(condition = product.condition)
            Spacer(modifier = Modifier.height(8.dp))
            ProductQuantities(
                availableQuantity = product.availableQuantity,
                soldQuantity = product.soldQuantity
            )
            Spacer(modifier = Modifier.height(16.dp))
            ProductAttributes(attributes = product.attributes)
        }
    } ?: run {
        state.error?.let { ErrorMessage(error = it) }
    }
}

@Composable
fun ProductTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun ProductPrice(price: Double, currencyId: String) {
    Text(
        text = "${price} ${currencyId}",
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun ProductCondition(condition: String) {
    Text(
        text = "Condition: $condition",
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun ProductQuantities(availableQuantity: Int, soldQuantity: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Available: $availableQuantity",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Sold: $soldQuantity",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ProductAttributes(attributes: List<Attribute>) {
    attributes.forEach { attribute ->
        Text(
            text = "${attribute.name}: ${attribute.value_name}",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}

@Composable
fun ErrorMessage(error: String) {
    Text(
        text = error,
        style = MaterialTheme.typography.bodyLarge,
        color = Color.Red
    )
}

@Composable
fun ProductImage(imageUrl: String) {
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Crop
    )
}