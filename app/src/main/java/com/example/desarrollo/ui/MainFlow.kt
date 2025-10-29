package com.example.desarrollo.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel // <-- 1. IMPORTANTE: Añade esta importación
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.desarrollo.viewmodel.CartViewModel // <-- 2. IMPORTANTE: Añade esta importación

// La definición de los items de la barra de navegación, hola martín 👽🌽
sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Catalog : BottomNavItem(AppDestinations.CATALOG_ROUTE, Icons.Default.Home, "Catálogo")
    object Cart : BottomNavItem(AppDestinations.CART_ROUTE, Icons.Default.ShoppingCart, "Carrito")
}

@Composable
fun MainFlow() {
    val navController = rememberNavController()
    val bottomNavItems = listOf(BottomNavItem.Catalog, BottomNavItem.Cart)

    // --- ¡AQUÍ ESTÁ LA LÓGICA CLAVE! ---
    // 3. Creamos la ÚNICA instancia del CartViewModel aquí, en el punto más alto.
    val cartViewModel: CartViewModel = viewModel()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        // Este es el NavHost para el contenido principal de la app
        NavHost(
            navController = navController,
            startDestination = AppDestinations.CATALOG_ROUTE,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppDestinations.CATALOG_ROUTE) {
                // 4. Pasamos la instancia ÚNICA a CatalogView.
                CatalogView(cartViewModel = cartViewModel)
            }
            composable(AppDestinations.CART_ROUTE) {
                // 5. Pasamos la MISMA instancia ÚNICA a CartScreen.
                CartScreen(cartViewModel = cartViewModel)
            }
        }
    }
}
