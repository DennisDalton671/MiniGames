package com.example.minigames.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.minigames.gamestate.AppViewModel
import com.example.minigames.ui.GameUI.Asteroids.AsteroidsGame_UI
import com.example.minigames.ui.GameUI.Asteroids.AsteroidsMenu_UI
import com.example.minigames.ui.GameUI.Flappy_Bird.FlappyBirdGame_UI
import com.example.minigames.ui.GameUI.Flappy_Bird.FlappyBirdMenu_UI
import com.example.minigames.ui.GameUI.Snake.SnakeGame_UI
import com.example.minigames.ui.GameUI.Snake.SnakeMenu_UI
import com.example.minigames.ui.GameUI.Tiny_Wings.TinyWingsGame_UI
import com.example.minigames.ui.GameUI.Tiny_Wings.TinyWingsMenu_UI
import com.example.minigames.ui.Menu_UI
import com.example.minigames.ui.Settings_UI

sealed class Screen(val route: String) {
    object Menu : Screen("menu")
    object Settings : Screen("settings")

    //Snake
    object SnakeMenu : Screen("snakemenu")
    object SnakeGame : Screen("snakegame")

    object AsteroidsMenu : Screen("asteroidsmenu")
    object AsteroidsGame: Screen("asteroidsgame")

    object FlappyBirdMenu : Screen("flappybirdmenu")
    object FlappyBirdGame: Screen("flappybirdgame")

    object TinyWingsMenu : Screen("tinywingmenu")
    object TinyWingsGame: Screen("tinywinggame")
}

@Composable
fun AppNavGraph(navController: NavHostController, appViewModel: AppViewModel) {
    NavHost(navController = navController, startDestination = Screen.Menu.route) {
        composable(Screen.Menu.route) {
            Menu_UI(navController, appViewModel)
        }

        composable(Screen.Settings.route) {
            Settings_UI(navController, appViewModel)
        }

        composable(Screen.SnakeMenu.route) {
            SnakeMenu_UI(navController, appViewModel)
        }

        composable(Screen.SnakeGame.route) {
            SnakeGame_UI(navController, appViewModel)
        }

        composable(Screen.AsteroidsMenu.route) {
            AsteroidsMenu_UI(navController, appViewModel)
        }

        composable(Screen.AsteroidsGame.route) {
            AsteroidsGame_UI(navController, appViewModel)
        }

        composable(Screen.FlappyBirdMenu.route) {
            FlappyBirdMenu_UI(navController, appViewModel)
        }

        composable(Screen.FlappyBirdGame.route) {
            FlappyBirdGame_UI(navController, appViewModel)
        }

        composable(Screen.TinyWingsMenu.route) {
            TinyWingsMenu_UI(navController, appViewModel)
        }

        composable(Screen.TinyWingsGame.route) {
            TinyWingsGame_UI(navController, appViewModel)
        }



    }
}