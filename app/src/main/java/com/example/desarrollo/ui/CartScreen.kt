package com.example.desarrollo.ui

import androidx.compose.foundation.Image // <-- 1. IMPORTACIÓN AÑADIDA
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape // <-- 2. IMPORTACIÓN AÑADIDA (para imágenes redondas)
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip // <-- 3. IMPORTACIÓN AÑADIDA
import androidx.compose.ui.res.painterResource // <-- 4. IMPORTACIÓN AÑADIDA
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.desarrollo.model.Product
import com.example.desarrollo.viewmodel.CartViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    cartViewModel: CartViewModel
) {
    // Escuchamos el uiState del ViewModel.
    val uiState by cartViewModel.uiState.collectAsState()

    // Estado para el diálogo de "Vaciar Carrito" (el que ya tenías 👽👽🌽🌽🌽🌽💩💩).
    var showClearCartDialog by rememberSaveable { mutableStateOf(false) }

    // estado para el diálogo de "Resumen de Compra".
    var showPurchaseDialog by rememberSaveable { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        if (uiState.productosEnCarrito.isEmpty()) {
            // Región para cuando el carrito está vacío
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
            // Región para cuando el carrito tiene productos
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "Mi Carrito",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )

                // Lista de productos
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(uiState.productosEnCarrito) { producto ->
                        CartItem(
                            producto = producto,
                            onAdd = { cartViewModel.agregarAlCarrito(producto) },
                            onRemove = { cartViewModel.removerUnidad(producto) }
                        )
                    }
                }

                TotalFooter(
                    total = uiState.total,
                    onClearCart = { showClearCartDialog = true },
                    onPurchase = { showPurchaseDialog = true }
                )
            }
        }
    }

    // "Vaciar Carrito" esta es la confirmación jajaajejeijojojuju.
    if (showClearCartDialog) {
        AlertDialog(
            onDismissRequest = {
                showClearCartDialog = false
            },
            title = {
                Text(text = "Confirmación")
            },
            text = {
                Text("¿Estás seguro/a de que quieres vaciar tu carrito?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        cartViewModel.vaciarCarrito()
                        showClearCartDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Vaciar")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        showClearCartDialog = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    //Resumen de Compra.
    if (showPurchaseDialog) {
        PurchaseSummaryDialog(
            products = uiState.productosEnCarrito,
            total = uiState.total,
            onDismiss = { showPurchaseDialog = false }
        )
    }
}


/**
 * Composable para el diálogo de resumen de compra, AHORA CON IMÁGENES.
 */
@Composable
fun PurchaseSummaryDialog(
    products: List<Product>,
    total: Int,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Resumen de tu Compra") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) { // Espacio entre cada producto
                Text("Comprarás:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                // Muestra cada producto en una línea separada
                products.forEach { product ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // IMAGEN PEQUEÑA Y REDONDA
                        Image(
                            painter = painterResource(id = product.imageRes),
                            contentDescription = "Imagen de ${product.name}",
                            modifier = Modifier
                                .size(32.dp) // Tamaño más pequeño
                                .clip(CircleShape) // Forma redonda
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        // TEXTO DEL PRODUCTO
                        val formattedUnit = getFormattedUnit(product.cantidad, product.unit)
                        Text(text = "${product.name} (${product.cantidad} $formattedUnit)")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Muestra el total formateado
                Text("Total: $${formatPrice(total)}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Por ahora, solo cierra el diálogo.
                    onDismiss()
                }
            ) {
                Text("Continuar a la Compra")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

/**
 * Función de ayuda para pluralizar las unidades.
 */
private fun getFormattedUnit(quantity: Int, unit: String): String {
    return if (quantity != 1 && unit.equals("kg", ignoreCase = true).not()) {
        "${unit}s"
    } else {
        unit
    }
}


/**
 * Composable para mostrar un solo item en el carrito, AHORA CON IMAGEN.
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
            // IMAGEN DEL PRODUCTO
            Image(
                painter = painterResource(id = producto.imageRes),
                contentDescription = producto.name,
                modifier = Modifier
                    .size(64.dp) // Tamaño mediano
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))

            // NOMBRE DEL PRODUCTO
            Text(
                text = producto.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )

            // CONTROLES DE CANTIDAD
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
 * Composable para el pie de página, AHORA CON DOS BOTONES.
 */
@Composable
fun TotalFooter(
    total: Int,
    onClearCart: () -> Unit,
    onPurchase: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Fila del total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total:", style = MaterialTheme.typography.titleLarge)

                Text(
                    text = "$${formatPrice(total)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            //Fila para los dos botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onClearCart,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Vaciar Carrito")
                }
                Button(
                    onClick = onPurchase,
                    modifier = Modifier.weight(2f)
                ) {
                    Text("Comprar")
                }
            }
        }
    }
}


private fun formatPrice(price: Int): String {
    // Especificamos la ruta completa para evitar ambigüedades con otras clases Locale
    return NumberFormat.getNumberInstance(java.util.Locale.GERMANY).format(price)
}
