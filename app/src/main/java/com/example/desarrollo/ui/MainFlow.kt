package com.example.desarrollo.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.desarrollo.MyApplication
import com.example.desarrollo.viewmodel.CartViewModel
import com.example.desarrollo.viewmodel.CartViewModelFactory
import com.example.desarrollo.viewmodel.CatalogViewModel
import com.example.desarrollo.viewmodel.MainViewModel
import com.example.desarrollo.viewmodel.ProfileViewModel
import com.example.desarrollo.viewmodel.ProfileViewModelFactory // Importa la nueva Factory

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Catalog : BottomNavItem(AppDestinations.CATALOG_ROUTE, Icons.Default.Home, "Catálogo")
    object Cart : BottomNavItem(AppDestinations.CART_ROUTE, Icons.Default.ShoppingCart, "Carrito")
    object Profile : BottomNavItem(AppDestinations.PROFILE_ROUTE, Icons.Default.AccountCircle, "Perfil")
}

@Composable
fun MainFlow(mainViewModel: MainViewModel, catalogViewModel: CatalogViewModel, onLogout: () -> Unit) {
    val navController = rememberNavController()
    val bottomNavItems = listOf(BottomNavItem.Catalog, BottomNavItem.Cart, BottomNavItem.Profile)

    val application = LocalContext.current.applicationContext as MyApplication
    val cartViewModel: CartViewModel = viewModel(
        factory = CartViewModelFactory(application.cartRepository)
    )
    // Usa la factory para crear el ProfileViewModel
    val profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory())

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
        NavHost(
            navController = navController,
            startDestination = AppDestinations.CATALOG_ROUTE,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppDestinations.CATALOG_ROUTE) {
                CatalogScreen(catalogViewModel = catalogViewModel, cartViewModel = cartViewModel)
            }
            composable(AppDestinations.CART_ROUTE) {
                CartScreen(cartViewModel = cartViewModel)
            }

            composable(AppDestinations.PROFILE_ROUTE) {
                ProfileScreen(
                    mainViewModel = mainViewModel,
                    profileViewModel = profileViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onLogout = onLogout
                )
            }
        }
    }
}
