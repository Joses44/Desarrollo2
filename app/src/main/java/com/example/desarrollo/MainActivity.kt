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
import com.example.desarrollo.ui.AuthDestinations
import com.example.desarrollo.ui.AuthNavigation
import com.example.desarrollo.ui.CatalogView
import com.example.desarrollo.ui.theme.theme.DesarrolloTheme

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


@Composable
fun AppNavigation() {

    val navController = rememberNavController()


    NavHost(
        navController = navController,
        startDestination = "auth_flow"
    ) {

        composable("auth_flow") {
            AuthNavigation(
                onAuthSuccess = {
                    navController.navigate(AuthDestinations.HOME_ROUTE) {

                        popUpTo("auth_flow") { inclusive = true }
                    }
                }
            )
        }


        composable(AuthDestinations.HOME_ROUTE) {

            CatalogView(
                modifier = Modifier.fillMaxSize()
            )
        }


    }
}
