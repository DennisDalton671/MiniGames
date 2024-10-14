package com.example.minigames.ui.GameUI.Snake

import android.content.pm.ActivityInfo
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.minigames.gamestate.AppViewModel
import com.example.minigames.ui.navigation.Screen
import com.example.minigames.ui.theme.AppTheme
import com.example.minigames.R
import com.example.minigames.ui.GameUI.BaseGameUI

@Composable
fun SnakeMenu_UI(navController: NavController, viewModel: AppViewModel) {
    val options = listOf("Play", "Back to Menu") // Specific options for the Snake game
    BackHandler(enabled = true) {
        navController.navigate(Screen.Menu.route)
        viewModel.startMenuMusic() // Navigate back after setting orientation
    }
    BaseGameUI(
        gameTitle = "Snake",
        options = options,
        textColor = Color.Green,
        onOptionSelected = { option ->
            when (option) {
                "Play" -> {
                    // Handle play logic
                    navController.navigate(Screen.SnakeGame.route)
                }
                "Back to Menu" -> {
                    // Go back to the main menu
                    viewModel.startMenuMusic()
                    navController.navigate(Screen.Menu.route)
                }
            }
        },
        borderColor = AppTheme.colors.neonGreen,
        gameDescription = "From ancient Nokia legends: Guide Snake, the original foodie, to endless snacks! But beware—the longer you grow, the harder it gets. Swipe to steer clear of your tail, grab food, and aim for the highest score in this endless classic!",
        videoResource = R.raw.snake_demo
    )
}