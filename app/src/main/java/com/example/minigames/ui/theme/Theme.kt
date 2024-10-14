package com.example.minigames.ui.theme

import androidx.compose.ui.graphics.Color

// Define your custom colors in the theme
val DarkGray = Color(0xFF2D2D2D)
val Gray = Color(0xFF808080)
val LightGray = Color(0xFFCCCCCC)
val Black = Color(0xFF000000)
val White = Color(0xFFFFFFFF)
val AccentRed = Color(0xFFB22222)
val NeonRed = Color(0xFFFF073A)
val NeonOrange = Color(0xFFFFA500)
val NeonYellow = Color(0xFFFFFF00)
val NeonGreen = Color(0xFF00FF00)
val NeonCyan = Color(0xFF00FFFF)
val FlappyYellow = Color(0xFFFDBE02)
val TinyBird = Color(0xFF007BA7)       // Cerulean Blue for the bird
val TinyBackground = Color(0xFF98FF98) // Mint Green for the background
val TinySlopes = Color(0xFFFFD700)     // Goldenrod Yellow for the slopes

// Define the colors for light and dark themes
val LightColors = AppColors(
    background = LightGray,
    contentBackground = White,
    borderColor = Black,
    buttonColor = AccentRed,
    textColor = Black,
    neonRed = NeonRed,
    neonOrange = NeonOrange,
    neonYellow = NeonYellow,
    neonGreen = NeonGreen,
    neonCyan = NeonCyan,
    dPad = Gray,
    flappyYellow = FlappyYellow,
    tinyBird = TinyBird, // Added TinyBird color
    tinyBackground = TinyBackground, // Added TinyBackground color
    tinySlopes = TinySlopes // Added TinySlopes color
)

val DarkColors = AppColors(
    background = DarkGray,
    contentBackground = Black,
    borderColor = White,
    buttonColor = AccentRed,
    textColor = White,
    neonRed = NeonRed,
    neonOrange = NeonOrange,
    neonYellow = NeonYellow,
    neonGreen = NeonGreen,
    neonCyan = NeonCyan,
    dPad = Gray,
    flappyYellow = FlappyYellow,
    tinyBird = TinyBird, // Added TinyBird color
    tinyBackground = TinyBackground, // Added TinyBackground color
    tinySlopes = TinySlopes // Added TinySlopes color
)

// Data class to hold the color palette
data class AppColors(
    val background: Color,
    val contentBackground: Color,
    val borderColor: Color,
    val buttonColor: Color,
    val textColor: Color,
    val neonRed: Color,
    val neonOrange: Color,
    val neonYellow: Color,
    val neonGreen: Color,
    val neonCyan: Color,
    val dPad: Color,
    val flappyYellow: Color,
    val tinyBird: Color, // Added TinyBird property
    val tinyBackground: Color, // Added TinyBackground property
    val tinySlopes: Color // Added TinySlopes property
)