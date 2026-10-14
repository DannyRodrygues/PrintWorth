package com.dannyrodrygues.printworth.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PrintWorthColors = lightColorScheme(
    primary = Color(0xFF007F73),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD9F0EC),
    onPrimaryContainer = Color(0xFF12343B),

    secondary = Color(0xFF12343B),
    onSecondary = Color.White,

    background = Color(0xFFF5F8FA),
    onBackground = Color(0xFF12343B),

    surface = Color.White,
    onSurface = Color(0xFF12343B),
    onSurfaceVariant = Color(0xFF52636A),

    outline = Color(0xFF71838A),

    error = Color(0xFFBA1A1A),
    onError = Color.White
)

@Composable
fun PrintWorthTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PrintWorthColors,
        content = content
    )
}