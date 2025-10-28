package com.example.desarrollo.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.desarrollo.model.Product
import com.example.desarrollo.model.SampleData


@Composable
fun CatalogView(modifier: Modifier = Modifier) {
    // LazyColumn es ideal para listas, ya que solo renderiza los elementos visibles.
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp) // Añade espacio a los lados de toda la lista
    ) {
        // Iteramos sobre cada 'category' en la lista de SampleData.categories
        SampleData.categories.forEach { category ->
            // 1. Mostramos el nombre de la categoría como un encabezado
            item {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.headlineSmall, // Estilo de título
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 24.dp, bottom = 8.dp) // Espacio arriba y abajo del título
                )
            }

            // 2. Mostramos los productos de esta categoría
            items(category.products) { product ->
                // Usamos el Composable ProductCard para mostrar cada producto
                ProductCard(product = product)
            }
        }
    }
}

/**
 * Este Composable define cómo se ve cada tarjeta de producto individual.
 * Recibe un objeto 'Product' completo para obtener todos sus datos.
 */
@Composable
fun ProductCard(product: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp) // Espacio entre las tarjetas
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically // Centra los elementos verticalmente
        ) {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = "Imagen de ${product.name}",
                modifier = Modifier
                    .size(80.dp) // Hacemos la imagen un poco más grande
                    .padding(end = 16.dp) // Espacio entre la imagen y el texto
            )
            Column(
                modifier = Modifier.weight(1f) // La columna de texto ocupa el espacio restante
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$${product.price} / ${product.unit}", // Muestra precio y unidad
                    style = MaterialTheme.typography.bodyLarge
                )
                // En el futuro, aquí podrías añadir un botón de "Añadir al carrito"
            }
        }
    }
}
