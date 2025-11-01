package com.example.desarrollo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.desarrollo.ui.AuthNavigation
import com.example.desarrollo.ui.MainFlow
import com.example.desarrollo.ui.theme.theme.DesarrolloTheme
import com.example.desarrollo.viewmodel.CatalogViewModel
import com.example.desarrollo.viewmodel.CatalogViewModelFactory
import com.example.desarrollo.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val catalogViewModel: CatalogViewModel by viewModels {
        CatalogViewModelFactory((application as MyApplication).productRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DesarrolloTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(catalogViewModel)
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
fun AppNavigation(catalogViewModel: CatalogViewModel) {
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = viewModel()
    val isLoggedIn by mainViewModel.isLoggedIn.collectAsState()

    val startDestination = AppFlow.AUTH_FLOW

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate(AppFlow.MAIN_FLOW) {
                popUpTo(AppFlow.AUTH_FLOW) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(AppFlow.AUTH_FLOW) {
            AuthNavigation(
                onAuthSuccess = {
                    mainViewModel.setLoggedIn()
                }
            )
        }

        composable(AppFlow.MAIN_FLOW) {
            MainFlow(
                catalogViewModel = catalogViewModel,
                onLogout = {
                    mainViewModel.setLoggedOut()
                    navController.navigate(AppFlow.AUTH_FLOW) {
                        popUpTo(AppFlow.MAIN_FLOW) { inclusive = true }
                    }
                }
            )
        }
    }
}
