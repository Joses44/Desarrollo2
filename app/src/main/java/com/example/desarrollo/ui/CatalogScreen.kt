package com.example.desarrollo.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.desarrollo.R
import com.example.desarrollo.model.Product
import com.example.desarrollo.viewmodel.CatalogViewModel
import com.example.desarrollo.viewmodel.CartViewModel
import com.example.desarrollo.viewmodel.ProductSyncState
import java.text.NumberFormat
import java.util.Locale

/**
 * 🎨 MAPEO DE IMÁGENES: Convierte los códigos del Backend/SampleData
 * en recursos reales de Android.
 */
fun getCatalogDrawableResource(imageResId: Int): Int {
    return when (imageResId) {
        101 -> R.drawable.manzanafuji
        102 -> R.drawable.naranjas
        103 -> R.drawable.platanos
        104 -> R.drawable.zanahoria
        105 -> R.drawable.espinaca
        106 -> R.drawable.pimiento
        107 -> R.drawable.miel
        108 -> R.drawable.quinoa
        109 -> R.drawable.lecheentera
        else -> R.drawable.ic_launcher_foreground
    }
}

@Composable
fun CatalogScreen(
    modifier: Modifier = Modifier,
    catalogViewModel: CatalogViewModel,
    cartViewModel: CartViewModel
) {
    val categoriesWithProducts by catalogViewModel.categoriesWithProducts.collectAsState()
    val syncState by catalogViewModel.syncState.collectAsState()

    val hasProducts = categoriesWithProducts.any { it.products.isNotEmpty() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = if (hasProducts) Arrangement.Top else Arrangement.Center
    ) {
        when (syncState) {
            is ProductSyncState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                Text("Cargando productos...", style = MaterialTheme.typography.bodyLarge)
            }

            is ProductSyncState.Error -> {
                val errorMessage = (syncState as ProductSyncState.Error).message
                ErrorStateView(
                    errorMessage = errorMessage,
                    hasData = hasProducts,
                    onRefresh = { catalogViewModel.refreshProducts() }
                )
                if (hasProducts) {
                    ProductList(categoriesWithProducts, cartViewModel)
                }
            }

            ProductSyncState.Success -> {
                if (categoriesWithProducts.isEmpty() && !hasProducts) {
                    Text(
                        "No se encontraron productos.",
                        modifier = Modifier.padding(top = 32.dp),
                        textAlign = TextAlign.Center
                    )
                } else {
                    ProductList(categoriesWithProducts, cartViewModel)
                }
            }
        }
    }
}

@Composable
fun ErrorStateView(
    errorMessage: String,
    hasData: Boolean,
    onRefresh: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(32.dp)
    ) {
        Text(
            text = "🚨 Error de Conexión",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Button(onClick = onRefresh) {
            Text("Reintentar")
        }
        if (hasData) {
            Text(
                "Mostrando datos locales.",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun ProductList(
    categoriesWithProducts: List<com.example.desarrollo.model.CategoryWithProducts>,
    cartViewModel: CartViewModel
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        categoriesWithProducts.forEach { categoryWithProducts ->
            // Solo mostrar la categoría si tiene productos
            if (categoryWithProducts.products.isNotEmpty()) {
                item {
                    Text(
                        text = categoryWithProducts.category.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
                    )
                }
                items(categoryWithProducts.products) { product ->
                    ProductCard(
                        product = product,
                        // ✅ CORRECCIÓN: Ahora pasamos SÓLO el ID del producto
                        onAddToCart = { cartViewModel.agregarAlCarrito(product.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 🎯 CORRECCIÓN: Usamos getCatalogDrawableResource para evitar el crash
            Image(
                painter = painterResource(id = getCatalogDrawableResource(product.imageRes)),
                contentDescription = "Imagen de ${product.name}",
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 16.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$${formatPrice(product.price)} / ${product.unit}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = product.rating.toString(), style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onAddToCart, modifier = Modifier.fillMaxWidth()) {
                    Text("Añadir unidad")
                }
            }
        }
    }
}

private fun formatPrice(price: Int): String {
    return NumberFormat.getNumberInstance(Locale.GERMANY).format(price)
}