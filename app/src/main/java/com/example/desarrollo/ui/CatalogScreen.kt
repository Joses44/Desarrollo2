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
import com.example.desarrollo.model.Product
import com.example.desarrollo.viewmodel.CatalogViewModel
import com.example.desarrollo.viewmodel.CartViewModel
import com.example.desarrollo.viewmodel.ProductSyncState // <-- ¡Importante!
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CatalogScreen(
    modifier: Modifier = Modifier,
    catalogViewModel: CatalogViewModel,
    cartViewModel: CartViewModel
) {
    // 1. Observar los datos (desde Room)
    val categoriesWithProducts by catalogViewModel.categoriesWithProducts.collectAsState()

    // 2. Observar el estado de sincronización (desde el Repositorio/API)
    val syncState by catalogViewModel.syncState.collectAsState()

    val hasProducts = categoriesWithProducts.any { it.products.isNotEmpty() }

    // Contenido central de la pantalla
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = if (hasProducts) Arrangement.Top else Arrangement.Center
    ) {

        // 🚀 LÓGICA DE MOSTRAR ESTADO:
        when (syncState) {
            is ProductSyncState.Loading -> {
                // Si está cargando, muestra el indicador en el centro
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))



                Text("Cargando productos del servidor...", style = MaterialTheme.typography.bodyLarge)
            }

            is ProductSyncState.Error -> {
                val errorMessage = (syncState as ProductSyncState.Error).message

                // Muestra el error de conexión
                ErrorStateView(
                    errorMessage = errorMessage,
                    hasData = hasProducts,
                    onRefresh = { catalogViewModel.refreshProducts() }
                )

                // Si hay datos locales, los muestra debajo del error
                if (hasProducts) {
                    ProductList(categoriesWithProducts, cartViewModel)
                }
            }

            ProductSyncState.Success -> {
                // Si la sincronización fue exitosa, muestra los productos o un mensaje si están vacíos
                if (categoriesWithProducts.isEmpty()) {
                    Text(
                        "No se encontraron productos en el servidor.",
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

// =========================================================
// 🔄 NUEVO COMPOSABLE PARA MANEJAR ESTADOS DE ERROR Y REINTENTO
// =========================================================

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
            text = "🚨 Error de Conexión 🚨",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(onClick = onRefresh) {
            Text("Reintentar Conexión")
        }

        if (hasData) {
            Text(
                "Mostrando datos locales desactualizados.",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}


// =========================================================
// 📦 COMPOSABLE AUXILIAR PARA LA LISTA DE PRODUCTOS
// =========================================================

@Composable
fun ProductList(
    categoriesWithProducts: List<com.example.desarrollo.model.CategoryWithProducts>,
    cartViewModel: CartViewModel
) {
    LazyColumn(
        // Remueve el padding horizontal del Column principal y lo aplica aquí
        modifier = Modifier.fillMaxSize()
    ) {
        categoriesWithProducts.forEach { categoryWithProducts ->
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
                    onAddToCart = { cartViewModel.agregarAlCarrito(product) }
                )
            }
        }
    }
}


// =========================================================
// 🖼️ PRODUCT CARD (Se mantiene igual)
// =========================================================

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
            // Asegúrate de que el ID de recurso de imagen sea válido
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = "Imagen de ${product.name}",
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 16.dp)
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$${formatPrice(product.price)} / ${product.unit}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = product.rating.toString(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onAddToCart,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Añadir unidad")
                }
            }
        }
    }
}

private fun formatPrice(price: Int): String {
    // Usamos Locale.GERMANY como ejemplo para el separador de miles
    return NumberFormat.getNumberInstance(Locale.GERMANY).format(price)
}
