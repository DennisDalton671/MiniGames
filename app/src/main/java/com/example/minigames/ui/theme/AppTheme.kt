package com.example.minigames.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

// Define the current theme colors as a composition local provider
val LocalAppColors = staticCompositionLocalOf { LightColors }

@Composable
fun AppTheme(
    isDarkTheme: Boolean = false,  // Add a flag to toggle between dark and light themes
    content: @Composable () -> Unit
) {
    // Choose between LightColors and DarkColors based on isDarkTheme flag
    val colors = if (isDarkTheme) DarkColors else LightColors

    // Provide the chosen theme colors to the rest of the app
    CompositionLocalProvider(LocalAppColors provides colors) {
        content()  // This wraps the UI in the provided colors
    }
}

// This is used to access the current colors across your app
object AppTheme {
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current
}