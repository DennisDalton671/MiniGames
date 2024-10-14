package com.example.minigames.ui.GameUI.Flappy_Bird

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.minigames.games.FlappyBird.Bird
import com.example.minigames.games.FlappyBird.Pipe
import com.example.minigames.gamestate.AppViewModel
import com.example.minigames.gamestate.GameState
import com.example.minigames.gamestate.game.FlappyBirdGameState
import com.example.minigames.ui.theme.AppTheme
import com.example.minigames.ui.GameUI.Components.RetroArcadeStatBar
import kotlinx.coroutines.delay

@Composable
fun FlappyBirdGame_UI(navController: NavController, viewModel: AppViewModel) {
    if (viewModel.gameState is FlappyBirdGameState) {
        val flappyBirdGameState = viewModel.gameState as FlappyBirdGameState

        var gameStarted by remember { mutableStateOf(false) }
        var canvasInitialized by remember { mutableStateOf(false) }
        var gameInitialized by remember { mutableStateOf(false) }


        // Launch game loop with 60 FPS
        LaunchedEffect(gameStarted) {
            if (gameStarted) {
                while (flappyBirdGameState.isRunning()) {
                    flappyBirdGameState.updateGame()
                    delay(16L) // 60 FPS
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.background)
        ) {
            // Display the score, time, and deaths
            RetroArcadeStatBar(
                score = flappyBirdGameState.currentHighScore,
                time = flappyBirdGameState.getElapsedTimeFormattedLive(),
                lives = flappyBirdGameState.deaths,
                Score = "High Score",
                Lives = "Deaths",
                textColor = AppTheme.colors.flappyYellow
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(BorderStroke(2.dp, Color(0xFFFDBE02)))
                    .background(AppTheme.colors.background),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height

                    // Set the canvas dimensions only once
                    if (!canvasInitialized) {
                        flappyBirdGameState.setCanvasDimensions(canvasWidth, canvasHeight)
                        canvasInitialized = true
                    }

                    // Initialize the game once when the game starts and canvas is initialized
                    if (gameStarted && !gameInitialized) {
                        flappyBirdGameState.initializeGame() // Initialize the game once
                        gameInitialized = true
                    }

                    // Only draw the game elements if the game is running
                    if (flappyBirdGameState.isRunning() && gameStarted) {
                        val bird = flappyBirdGameState.bird
                        drawCircle(
                            color = Color.Yellow,
                            radius = bird.width / 2,
                            center = Offset(bird.x + bird.width / 2, bird.y + bird.height / 2)
                        )

                        flappyBirdGameState.pipes.forEach { pipe ->
                            drawRect(
                                color = Color.Green,
                                topLeft = Offset(pipe.x, 0f),
                                size = Size(flappyBirdGameState.getPipeWidth(), pipe.height)
                            )
                            drawRect(
                                color = Color.Green,
                                topLeft = Offset(pipe.x, pipe.height + pipe.gap),
                                size = Size(flappyBirdGameState.getPipeWidth(), canvasHeight - (pipe.height + pipe.gap))
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.40f)
                    .border(BorderStroke(2.dp, Color(0xFFFDBE02)))
                    .background(AppTheme.colors.contentBackground)
                    .clickable {
                        if (!gameStarted && canvasInitialized) {
                            gameStarted = true // Set game started flag when tapped
                            flappyBirdGameState.resumeGame() // Start the game by resuming
                        } else if (flappyBirdGameState.isRunning()) {
                            flappyBirdGameState.birdFlap() // Make the bird flap if the game is running
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Touch to Flap!",
                    color = AppTheme.colors.textColor,
                    style = TextStyle(fontSize = 18.sp)
                )
            }
        }
    }
}