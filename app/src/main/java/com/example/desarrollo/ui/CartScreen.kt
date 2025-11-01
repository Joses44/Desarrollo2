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

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    cartViewModel: CartViewModel
) {
    val uiState by cartViewModel.uiState.collectAsState()
    var showClearCartDialog by rememberSaveable { mutableStateOf(false) }
    var showPurchaseDialog by rememberSaveable { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<CartItemDetails?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        if (uiState.items.isEmpty()) {
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
                    items(uiState.items) { item ->
                        CartItem(
                            item = item,
                            onIncrement = { cartViewModel.incrementarUnidad(item.productId) },
                            onDecrement = { cartViewModel.decrementarUnidad(item.productId) },
                            onRemove = { itemToDelete = item }
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
            total = uiState.total,
            onDismiss = { showPurchaseDialog = false }
        )
    }

    itemToDelete?.let {
        ConfirmarEliminacionDialog(
            item = it,
            onConfirm = {
                cartViewModel.removerProducto(it.productId)
                itemToDelete = null
            },
            onDismiss = { itemToDelete = null }
        )
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
    total: Int,
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
                Text("Total: $${formatPrice(total)}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
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

private fun getFormattedUnit(quantity: Int, unit: String): String {
    return if (quantity != 1 && unit.equals("kg", ignoreCase = true).not()) {
        "${unit}s"
    } else {
        unit
    }
}

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
                Text(
                    text = "$${formatPrice(item.price * item.quantity)}",
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
    total: Int,
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
                    text = "$${formatPrice(total)}",
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

private fun formatPrice(price: Int): String {
    return NumberFormat.getNumberInstance(Locale.GERMANY).format(price)
}
