package com.example.desarrollo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.desarrollo.ui.AuthNavigation
import com.example.desarrollo.ui.MainFlow // <-- ¡IMPORTANTE! Importa el nuevo MainFlow
import com.example.desarrollo.ui.theme.theme.DesarrolloTheme // Ajusta si la ruta del tema es otra

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DesarrolloTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

// Define los nombres para los flujos principales
object AppFlow {
    const val AUTH_FLOW = "auth_flow"
    const val MAIN_FLOW = "main_flow"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppFlow.AUTH_FLOW // Empezamos en el flujo de autenticación
    ) {
        // Flujo de Autenticación
        composable(AppFlow.AUTH_FLOW) {
            AuthNavigation(
                onAuthSuccess = {
                    // Cuando el login es exitoso, navegamos al MainFlow
                    navController.navigate(AppFlow.MAIN_FLOW) {
                        // Limpiamos el historial de navegación para que el usuario no pueda
                        // volver a la pantalla de login con el botón de "atrás".
                        popUpTo(AppFlow.AUTH_FLOW) { inclusive = true }
                    }
                }
            )
        }

        // Flujo Principal de la App
        composable(AppFlow.MAIN_FLOW) {
            MainFlow() // Aquí se carga toda la interfaz principal (catálogo, carrito, y la barra de nav)
        }
    }
}
