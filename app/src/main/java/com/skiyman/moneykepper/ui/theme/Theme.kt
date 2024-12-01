package com.skiyman.moneykepper.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Цвета для светлой темы
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF7FFFD4),
    onPrimary = Color.Black,
    secondary = Color(0xFF5FD7C1),
    onSecondary = Color.White,
    background = Color(0xFFE0F2F1),
    onBackground = Color.Black,
    surface = Color(0xFFFFFFFF),
    onSurface = Color.Black
)

// Цвета для тёмной темы
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFA500),
    onPrimary = Color.Black,
    secondary = Color(0xFFFFCC99),
    onSecondary = Color.Black,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White
)

@Composable
fun MoneyKepperTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}