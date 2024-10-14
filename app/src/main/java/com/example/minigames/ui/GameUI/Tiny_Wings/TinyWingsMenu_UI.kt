package com.example.minigames.ui.GameUI.Tiny_Wings

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.minigames.gamestate.AppViewModel
import com.example.minigames.ui.navigation.Screen
import com.example.minigames.ui.theme.AppTheme
import com.example.minigames.R
import com.example.minigames.ui.GameUI.BaseGameUI

@Composable
fun TinyWingsMenu_UI(navController: NavController, viewModel: AppViewModel) {
    val options = listOf("Play", "Back to Menu")
    // Listen for back navigation to set menu music
    BackHandler(enabled = true) {
        navController.navigate(Screen.Menu.route)
        viewModel.startMenuMusic() // Navigate back after setting orientation
    }
    BaseGameUI(
    gameTitle = "Tiny Wings",
    options = options,
    textColor = AppTheme.colors.tinyBird,
    onOptionSelected = { option ->
        when (option) {
            "Play" -> {
                // Handle play logic
                navController.navigate(Screen.TinyWingsGame.route)
            }
            "Back to Menu" -> {
                // Go back to the main menu
                viewModel.startMenuMusic()
                navController.navigate(Screen.Menu.route)
            }
        }
    },
    borderColor = AppTheme.colors.tinyBird,
    gameDescription = "Remember when every move had to be perfect? Tiny Wings takes you back to those retro days where hills were your launchpads and the sky was your limit. Hit the slopes, soar high, and chase that high score!",
    videoResource = R.raw.flappybird_demo
    )
}