package com.example.desarrollo.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.desarrollo.model.Product
import com.example.desarrollo.viewmodel.CartViewModel

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    cartViewModel: CartViewModel
) {
    // 1. Escuchamos el uiState y lo convertimos en un State de Compose.
    val uiState by cartViewModel.uiState.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        if (uiState.productosEnCarrito.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Tu carrito está vacío",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "Mi Carrito",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )

                LazyColumn(modifier = Modifier.weight(1f)) {
                    // 2. Usamos la lista de productos del nuevo uiState.
                    items(uiState.productosEnCarrito) { producto ->
                        CartItem(
                            producto = producto,
                            onAdd = { cartViewModel.agregarAlCarrito(producto) },
                            onRemove = { cartViewModel.removerUnidad(producto) }
                        )
                    }
                }

                // 3. Usamos el total del nuevo uiState.
                TotalFooter(
                    total = uiState.total,
                    onClearCart = { cartViewModel.vaciarCarrito() }
                )
            }
        }
    }
}

// El resto del archivo (CartItem y TotalFooter) no necesita cambios.
// Pega el código que ya tenías para esas funciones aquí abajo si los borraste.

/**
 * Composable para mostrar un solo item en la lista del carrito. (SIN CAMBIOS)
 */
@Composable
fun CartItem(
    producto: Product,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = producto.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(onClick = onRemove, modifier = Modifier.size(40.dp), contentPadding = PaddingValues(0.dp)) {
                    Text("-", style = MaterialTheme.typography.titleLarge)
                }
                Text(
                    text = "${producto.cantidad}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                OutlinedButton(onClick = onAdd, modifier = Modifier.size(40.dp), contentPadding = PaddingValues(0.dp)) {
                    Text("+", style = MaterialTheme.typography.titleLarge)
                }
            }
        }
    }
}

/**
 * Composable para el pie de página. (SIN CAMBIOS)
 */
@Composable
fun TotalFooter(total: Double, onClearCart: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total:", style = MaterialTheme.typography.titleLarge)
                Text(
                    "$${"%.2f".format(total)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onClearCart,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Vaciar Carrito")
            }
        }
    }
}
