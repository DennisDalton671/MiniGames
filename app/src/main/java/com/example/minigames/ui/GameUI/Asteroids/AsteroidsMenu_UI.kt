package com.example.minigames.ui.GameUI.Asteroids

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.minigames.gamestate.AppViewModel
import com.example.minigames.ui.navigation.Screen
import com.example.minigames.ui.theme.AppTheme
import com.example.minigames.R
import com.example.minigames.ui.GameUI.BaseGameUI

@Composable
fun AsteroidsMenu_UI(navController: NavController, viewModel: AppViewModel) {
    BackHandler(enabled = true) {
        navController.navigate(Screen.Menu.route)
        viewModel.startMenuMusic() // Navigate back after setting orientation
    }
    val options = listOf("Play", "Back to Menu") // Specific options for the Snake game
    BaseGameUI(
        gameTitle = "Asteroids",
        options = options,
        textColor = AppTheme.colors.textColor,
        onOptionSelected = { option ->
            when (option) {
                "Play" -> {
                    // Handle play logic
                    navController.navigate(Screen.AsteroidsGame.route)
                }
                "Back to Menu" -> {
                    // Go back to the main menu
                    viewModel.startMenuMusic()
                    navController.navigate(Screen.Menu.route)
                }
            }
        },
        borderColor = AppTheme.colors.textColor,
        gameDescription = "Out here in the cold vacuum of space, it’s just you, a lot of asteroids, and your ship’s commitment to never stop firing. You can dodge rocks all day long, but don’t try to do the Kessel Run in under 12 parsecs—you’re here for survival!",
        videoResource = R.raw.asteroids_demo
    )
}