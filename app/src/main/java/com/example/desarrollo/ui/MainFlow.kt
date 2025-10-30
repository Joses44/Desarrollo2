package com.example.desarrollo.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.AccountCircle // 🌟 AGREGAR: Icono de perfil
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.desarrollo.viewmodel.CartViewModel
import com.example.desarrollo.viewmodel.ProfileViewModel // 🌟 AGREGAR: ViewModel de Perfil

// La definición de los items de la barra de navegación, hola martín 👽🌽
sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Catalog : BottomNavItem(AppDestinations.CATALOG_ROUTE, Icons.Default.Home, "Catálogo")
    object Cart : BottomNavItem(AppDestinations.CART_ROUTE, Icons.Default.ShoppingCart, "Carrito")
    object Profile : BottomNavItem(AppDestinations.PROFILE_ROUTE, Icons.Default.AccountCircle, "Perfil") // 🌟 AGREGAR: Nuevo ítem de perfil
}

@Composable
fun MainFlow() {
    val navController = rememberNavController()
    // 🌟 MODIFICADO: Incluir el nuevo ítem de perfil en la lista
    val bottomNavItems = listOf(BottomNavItem.Catalog, BottomNavItem.Cart, BottomNavItem.Profile)

    // --- ¡AQUÍ ESTÁ LA LÓGICA CLAVE! ---
    // 3. ViewModels con alcance de MainFlow
    val cartViewModel: CartViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel() // 🌟 AGREGAR: Instancia de ProfileViewModel

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
                // 4. Sin cambios en la firma de CatalogView
                CatalogView(cartViewModel = cartViewModel)
            }
            composable(AppDestinations.CART_ROUTE) {
                // 5. Sin cambios en la firma de CartScreen
                CartScreen(cartViewModel = cartViewModel)
            }

            // 🌟 AGREGAR: Nuevo destino para la pantalla de perfil
            composable(AppDestinations.PROFILE_ROUTE) {
                ProfileScreen(
                    viewModel = profileViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
