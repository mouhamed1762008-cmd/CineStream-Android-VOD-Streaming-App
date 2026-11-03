package com.cinestream.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CineStreamColorScheme =
  darkColorScheme(
    primary = CinePrimary,
    onPrimary = Color.White,
    primaryContainer = CineAccentGlow,
    onPrimaryContainer = Color.White,
    secondary = CineSecondary,
    onSecondary = CineBackground,
    secondaryContainer = CineSurfaceVariant,
    onSecondaryContainer = CineTextPrimary,
    tertiary = CineTertiary,
    background = CineBackground,
    onBackground = CineTextPrimary,
    surface = CineSurface,
    onSurface = CineTextPrimary,
    surfaceVariant = CineSurfaceVariant,
    onSurfaceVariant = CineTextSecondary,
    outline = CineBorder,
  )

@Composable
fun CineStreamTheme(
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = CineStreamColorScheme,
    typography = Typography,
    content = content
  )
}

// Retain alias for compatibility
@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  CineStreamTheme(content = content)
}
