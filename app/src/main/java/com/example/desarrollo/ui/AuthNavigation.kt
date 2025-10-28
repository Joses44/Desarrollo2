package com.example.desarrollo.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.desarrollo.viewmodel.AuthViewModel

object AuthDestinations {
    const val LOGIN_ROUTE = "login"
    const val REGISTER_ROUTE = "register"
    const val HOME_ROUTE = "home" // Asume que tienes una pantalla principal
}

@Composable
fun AuthNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = viewModel(),
    onAuthSuccess: () -> Unit // Callback para cuando el login/registro lleva a la pantalla principal
) {
    NavHost(
        navController = navController,
        startDestination = AuthDestinations.LOGIN_ROUTE,
        modifier = modifier
    ) {
        composable(AuthDestinations.LOGIN_ROUTE) {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = { onAuthSuccess() },
                onNavigateToRegister = { navController.navigate(AuthDestinations.REGISTER_ROUTE) }
            )
        }
        composable(AuthDestinations.REGISTER_ROUTE) {
            SignUpScreen(
                authViewModel = authViewModel,
                onRegistrationSuccess = { navController.navigate(AuthDestinations.LOGIN_ROUTE) }, // Después del registro, volvemos a Login
                onNavigateToLogin = { navController.navigate(AuthDestinations.LOGIN_ROUTE) }
            )
        }
        // Aquí podrías añadir el composable de tu pantalla principal (ej. CatalogScreen)
        composable(AuthDestinations.HOME_ROUTE) {
            // CatalogScreen() // Una vez autenticado, iría aquí
            Text("Bienvenido a la pantalla principal!") // Placeholder
        }
    }
}