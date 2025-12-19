package com.example.desarrollo.ui


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.desarrollo.model.CartItemDetails
import com.example.desarrollo.viewmodel.CartViewModel
import java.text.NumberFormat
import java.util.Locale

// --- FUNCIONES DE SOPORTE ---

// Formatea el precio utilizando Double, acorde al backend
private fun formatPrice(price: Double): String {
    // Usamos el formato de moneda local.
    // Si necesitas un formato específico (ej. USD sin símbolo), ajusta el Locale/Currency.
    return NumberFormat.getCurrencyInstance(Locale.getDefault()).format(price)
}

private fun getFormattedUnit(quantity: Int, unit: String): String {
    return if (quantity != 1 && unit.equals("kg", ignoreCase = true).not()) {
        "${unit}s"
    } else {
        unit
    }
}

// --- PANTALLA PRINCIPAL ---

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    cartViewModel: CartViewModel
) {
    // Observamos el estado del ViewModel (que incluye items, total, loading, error)
    val uiState by cartViewModel.uiState.collectAsState()

    // Estados para los diálogos
    var showClearCartDialog by rememberSaveable { mutableStateOf(false) }
    var showPurchaseDialog by rememberSaveable { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<CartItemDetails?>(null) }

    Box(modifier = modifier.fillMaxSize()) {

        // 1. Manejo de Carga y Error
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (uiState.errorMessage != null) {
            // Muestra error y un botón de reintento
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Error al cargar el carrito: ${uiState.errorMessage}", color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
                Button(onClick = { cartViewModel.fetchCart() }) {
                    Text("Reintentar")
                }
            }
        }

        // 2. Contenido Principal (Carrito Lleno o Vacío)
        else if (uiState.items.isEmpty()) {
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
                    items(uiState.items, key = { it.productId }) { item -> // Usar el ID del CartItem como clave
                        CartItem(
                            item = item,
                            onIncrement = { cartViewModel.incrementarUnidad(item.productId) },
                            onDecrement = { cartViewModel.decrementarUnidad(item.productId) },
                            onRemove = { itemToDelete = item }
                        )
                    }
                }

                TotalFooter(
                    total = uiState.total, // Total ahora es Double
                    onClearCart = { showClearCartDialog = true },
                    onPurchase = { showPurchaseDialog = true }
                )
            }
        }
    }

    // --- DIÁLOGOS ---

    if (showClearCartDialog) {
        AlertDialog(
            onDismissRequest = { showClearCartDialog = false },
            title = { Text(text = "Confirmación") },
            text = { Text("¿Estás seguro/a de que quieres vaciar tu carrito?") },
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
                OutlinedButton(onClick = { showClearCartDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showPurchaseDialog) {
        PurchaseSummaryDialog(
            items = uiState.items,
            total = uiState.total, // Total ahora es Double
            onDismiss = { showPurchaseDialog = false }
        )
    }

    itemToDelete?.let {
        ConfirmarEliminacionDialog(
            item = it,
            onConfirm = {
                // CORRECCIÓN CLAVE: Usamos el ID de la línea de carrito (CartItemDetails.id)
                cartViewModel.removerProducto(it.productId)
                itemToDelete = null
            },
            onDismiss = { itemToDelete = null }
        )
    }
}

// --- COMPONENTES REUTILIZABLES ---

@Composable
fun CartItem(
    item: CartItemDetails,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
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
            // Nota: Debes asegurar que tu CartItemDetails tenga imageRes o imageUrl
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.name,
                modifier = Modifier.size(64.dp).clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Asegúrate de que item.price sea Double para esta multiplicación,
                // o haz un cast antes de la multiplicación.
                val itemTotalPrice = item.price * item.quantity
                Text(
                    text = formatPrice(itemTotalPrice.toDouble()), // Formateamos el subtotal
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                OutlinedButton(onClick = onDecrement, modifier = Modifier.size(40.dp), contentPadding = PaddingValues(0.dp)) {
                    Icon(Icons.Default.Remove, contentDescription = "Quitar unidad")
                }
                Text(
                    text = "${item.quantity}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                OutlinedButton(onClick = onIncrement, modifier = Modifier.size(40.dp), contentPadding = PaddingValues(0.dp)) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir unidad")
                }
                IconButton(onClick = onRemove, modifier = Modifier.size(40.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar producto")
                }
            }
        }
    }
}

@Composable
fun TotalFooter(
    total: Double, // Ahora Double
    onClearCart: () -> Unit,
    onPurchase: () -> Unit
) {
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
                    text = formatPrice(total), // Usamos la función actualizada
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
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
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Comprar")
                }
            }
        }
    }
}

@Composable
fun ConfirmarEliminacionDialog(
    item: CartItemDetails,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmar Eliminación") },
        text = {
            val formattedUnit = getFormattedUnit(item.quantity, item.unit)
            Text("Eliminarás todas las unidades del producto \"${item.name} (${item.quantity} $formattedUnit)\".")
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Eliminar Unidades")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun PurchaseSummaryDialog(
    items: List<CartItemDetails>,
    total: Double, // Ahora Double
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Resumen de tu Compra") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Comprarás:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                items.forEach { item ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = item.imageRes),
                            contentDescription = "Imagen de ${item.name}",
                            modifier = Modifier.size(32.dp).clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        val formattedUnit = getFormattedUnit(item.quantity, item.unit)
                        Text(text = "${item.name} (${item.quantity} $formattedUnit)")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Total: ${formatPrice(total)}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
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


