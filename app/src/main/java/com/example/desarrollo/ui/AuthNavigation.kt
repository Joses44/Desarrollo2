package com.example.desarrollo.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.desarrollo.viewmodel.AuthViewModel

object AuthDestinations {
    const val LOGIN_ROUTE = "login"
    const val REGISTER_ROUTE = "register"
}

@Composable
// 💡 CAMBIO CRUCIAL: onAuthSuccess ahora espera el 'token: String'
fun AuthNavigation(onAuthSuccess: (token: String) -> Unit) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    NavHost(navController = navController, startDestination = AuthDestinations.LOGIN_ROUTE) {
        composable(AuthDestinations.LOGIN_ROUTE) {
            LoginScreen(
                authViewModel = authViewModel,
                // onLoginSuccess ahora debe pasar el token que recibe a onAuthSuccess
                onLoginSuccess = onAuthSuccess,
                onNavigateToRegister = { navController.navigate(AuthDestinations.REGISTER_ROUTE) }
            )
        }
        composable(AuthDestinations.REGISTER_ROUTE) {
            SignUpScreen(
                authViewModel = authViewModel,
                // Generalmente, el registro exitoso simplemente vuelve al login para iniciar sesión
                onRegistrationSuccess = { navController.popBackStack() },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }
    }
}