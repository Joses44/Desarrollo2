// 1. Ahora la declaración del paquete es la correcta
package com.example.desarrollo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.desarrollo.ui.CatalogView
import com.example.desarrollo.ui.theme.theme.DesarrolloTheme // Ajusta si el tema tiene otra ruta

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DesarrolloTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CatalogView(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
