
package com.dannyrodrygues.printworth.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Tema claro — identidade oficial PrintWorth
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF007F73),
    onPrimary = Color.White,

    primaryContainer = Color(0xFFD9F0EC),
    onPrimaryContainer = Color(0xFF12343B),

    secondary = Color(0xFF007F73),
    onSecondary = Color.White,

    background = Color(0xFFF5F8FA),
    onBackground = Color(0xFF12343B),

    surface = Color.White,
    onSurface = Color(0xFF12343B),
    onSurfaceVariant = Color(0xFF52636A),

    surfaceContainer = Color(0xFFE9EFF2),

    outline = Color(0xFF71838A),

    error = Color(0xFFBA1A1A),
    onError = Color.White
)

// Tema escuro — preserva as cores da tela aprovada
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF0A8F87),
    onPrimary = Color.White,

    primaryContainer = Color(0xFF1A1A1D),
    onPrimaryContainer = Color(0xFFE8EEF0),

    secondary = Color(0xFF23B7AD),
    onSecondary = Color.Black,

    background = Color(0xFF000000),
    onBackground = Color(0xFFD7E1E5),

    surface = Color(0xFF0B0B0D),
    onSurface = Color(0xFFE8EEF0),
    onSurfaceVariant = Color(0xFF96A6AE),

    surfaceContainer = Color(0xFF1A1A1D),

    outline = Color(0xFF52636A),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

@Composable
fun PrintWorthTheme(
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
        content = content
    )
}
