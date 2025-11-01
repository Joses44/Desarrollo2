package com.example.desarrollo.ui.theme.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = VerdeEsmeralda,
    secondary = AmarilloMostaza,
    background = BlancoSuave,
    surface = BlancoSuave,
    onPrimary = Color.White,
    onSecondary = GrisOscuro,
    onBackground = GrisOscuro,
    onSurface = GrisOscuro,
)

private val DarkColorScheme = darkColorScheme(
    primary = VerdeEsmeralda,
    secondary = AmarilloMostaza,
    background = GrisOscuro,
    surface = GrisOscuro,
    onPrimary = Color.White,
    onSecondary = GrisOscuro,
    onBackground = BlancoSuave,
    onSurface = BlancoSuave,
)

@Composable
fun DesarrolloTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
