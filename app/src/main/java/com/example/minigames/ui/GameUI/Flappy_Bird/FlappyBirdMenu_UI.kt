package com.example.minigames.ui.GameUI.Flappy_Bird

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.minigames.gamestate.AppViewModel
import com.example.minigames.ui.navigation.Screen
import com.example.minigames.ui.theme.AppTheme
import com.example.minigames.R
import com.example.minigames.ui.GameUI.BaseGameUI


@Composable
fun FlappyBirdMenu_UI(navController: NavController, viewModel: AppViewModel) {
    BackHandler(enabled = true) {
        navController.navigate(Screen.Menu.route)
        viewModel.startMenuMusic() // Navigate back after setting orientation
    }
    val options = listOf("Play", "Back to Menu") // Specific options for the Snake game
    BaseGameUI(
    gameTitle = "Flappy Bird",
    options = options,
    textColor = AppTheme.colors.flappyYellow,
    onOptionSelected = { option ->
        when (option) {
            "Play" -> {
                // Handle play logic
                navController.navigate(Screen.FlappyBirdGame.route)
            }
            "Back to Menu" -> {
                // Go back to the main menu
                viewModel.startMenuMusic()
                navController.navigate(Screen.Menu.route)
            }
        }
    },
    borderColor = AppTheme.colors.flappyYellow,
    gameDescription = "Tap, flap, repeat… and don't crash! Navigate your way through a labyrinth of suspiciously placed pipes. Originally thought to be impossible, only true legends can master the art of the perfect flap. How many pipes can you pass before the rage kicks in?",
    videoResource = R.raw.flappybird_demo
    )
}