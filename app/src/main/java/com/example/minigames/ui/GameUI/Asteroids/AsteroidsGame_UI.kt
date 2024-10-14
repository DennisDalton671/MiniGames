package com.example.minigames.ui.GameUI.Asteroids

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.minigames.ui.theme.AppTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.minigames.games.Asteroids.Asteroid
import com.example.minigames.games.Asteroids.Bullet
import com.example.minigames.games.Asteroids.Spaceship
import com.example.minigames.gamestate.AppViewModel
import com.example.minigames.gamestate.game.AsteroidsGameState
import com.example.minigames.ui.GameUI.Components.RetroArcadeStatBar
import com.example.minigames.ui.GameUI.Components.SwipeToControlText
import kotlinx.coroutines.delay
import kotlin.math.atan2
import kotlin.math.hypot

@Composable
fun AsteroidsGame_UI(navController: NavController, viewModel: AppViewModel) {
    // Ensure the correct game state is used
    if (viewModel.gameState is AsteroidsGameState) {
        val asteroidsGameState = viewModel.gameState as AsteroidsGameState
        var hasInteracted by remember { mutableStateOf(false) } // Track if user has interacted
        var gameStarted by remember { mutableStateOf(false) }   // Track if the game has started

        val infiniteTransition = rememberInfiniteTransition()
        val neonPulse by infiniteTransition.animateFloat(
            initialValue = 0.6f,
            targetValue = 1.4f,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )

        // Function to adjust brightness for the neon glow effect
        fun adjustBrightness(color: Color, factor: Float): Color {
            return Color(
                red = (color.red * factor).coerceIn(0f, 1f),
                green = (color.green * factor).coerceIn(0f, 1f),
                blue = (color.blue * factor).coerceIn(0f, 1f)
            )
        }

        val neonWhite = adjustBrightness(AppTheme.colors.textColor, neonPulse)
        val darkBackground = AppTheme.colors.contentBackground

        // Game loop to move the spaceship and asteroids
        LaunchedEffect(gameStarted) {
            if (gameStarted) {
                while (!asteroidsGameState.isAGameOver) {
                    asteroidsGameState.updateGame() // Update game state
                    delay(16L) // 60 FPS for smooth movement
                }

                // Handle game over logic and reset
                if (asteroidsGameState.isAGameOver) {
                    delay(1000L) // Delay to allow user to see game over state

                    // Reset game state and flags
                    asteroidsGameState.resetGameA() // Reset the game state
                    hasInteracted = false // Reset interaction flag
                    gameStarted = false // Reset game started flag
                }
            }
        }

        // Render game screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.background)
        ) {
            // Game stats (top section)
            RetroArcadeStatBar(
                score = asteroidsGameState.score,
                time = asteroidsGameState.getElapsedTimeFormatted(),
                lives = asteroidsGameState.lives,
                Score = "Score",
                Lives = "Lives",
                textColor = AppTheme.colors.textColor
            )

            // Game canvas (middle section)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(BorderStroke(2.dp, neonWhite))
                    .background(AppTheme.colors.background),
                contentAlignment = Alignment.Center
            ) {
                // Show game elements only after the user has interacted
                if (hasInteracted) {
                    AsteroidsCanvas(asteroidsGameState, AppTheme.colors.background) // Render the game
                } else {
                    // Optionally, show a placeholder or blank screen
                    Text(
                        text = "Waiting for input...",
                        color = AppTheme.colors.textColor,
                        style = TextStyle(fontSize = 24.sp)
                    )
                }
            }

            // Touch controls (bottom section)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.40f)
                    .border(BorderStroke(2.dp, neonWhite))
                    .background(darkBackground)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            val swipeCenter = size.center // Get the center of the swipe area
                            val touchPosition = change.position // Current touch position

                            // Calculate the angle and distance from the center
                            val dx = touchPosition.x - swipeCenter.x
                            val dy = touchPosition.y - swipeCenter.y
                            val distance = hypot(dx, dy)
                            val angle = atan2(dy, dx) * (180 / Math.PI).toFloat()

                            // Normalize the distance to control speed
                            val maxDistance = minOf(size.width, size.height) / 2
                            val normalizedSpeed = (distance / maxDistance).coerceIn(0f, 1f) * 10

                            change.consume()
                            hasInteracted = true // Set to true once user interacts

                            Log.d("Touch Input", "Angle: $angle, Speed: $normalizedSpeed") // Add this log

                            // Start the game loop after the first interaction
                            if (!gameStarted) {
                                asteroidsGameState.startNewGame()
                                gameStarted = true
                            }

                            // Pass angle and speed to the game logic
                            asteroidsGameState.updateSpaceshipDirectionAndSpeed(angle, normalizedSpeed)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                // Only show the swipe text if the user hasn't interacted yet
                if (!hasInteracted) {
                    SwipeToControlText(neonColor = AppTheme.colors.textColor)
                }
            }
        }
    }
}

@Composable
fun AsteroidsCanvas(asteroidsGameState: AsteroidsGameState, backgroundColor: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        // Pass the canvas size to the game state
        val canvasWidth = size.width
        val canvasHeight = size.height

        // Inform GameState about the canvas dimensions
        asteroidsGameState.setCanvasDimensions(canvasWidth, canvasHeight)

        // Render spaceship, asteroids, and bullets based on GameState
        drawSpaceship(asteroidsGameState.spaceship, backgroundColor = backgroundColor)

        asteroidsGameState.asteroids.forEach { asteroid ->
            drawAsteroid(asteroid)
        }

        asteroidsGameState.bullets.forEach { bullet ->
            drawBullet(bullet)
        }
    }
}

fun DrawScope.drawSpaceship(spaceship: Spaceship, backgroundColor: Color) {
    if (spaceship.isVisible) {
    val shipPosition = Offset(spaceship.x, spaceship.y)

    // Rotate the ship, adding 90 degrees to align the tip with the movement direction
    rotate(degrees = spaceship.direction + 90f, pivot = shipPosition) {
        // Define the triangle shape for the ship with the tip at the top
        val spaceshipPath = Path().apply {
            moveTo(
                shipPosition.x,
                shipPosition.y - spaceship.size * 1.5f
            )  // Tip of the ship (front)
            lineTo(shipPosition.x - spaceship.size, shipPosition.y + spaceship.size)  // Bottom left
            lineTo(
                shipPosition.x + spaceship.size,
                shipPosition.y + spaceship.size
            )  // Bottom right
            close()
        }

        // Draw the spaceship's body (larger triangle)
        drawPath(
            path = spaceshipPath,
            color = Color.White,  // Ship color
            style = Fill
        )

        // Draw a circle at the back of the ship for a rounded tail
        drawCircle(
            color = backgroundColor,  // Same color as the background to "cut out" the rounded tail
            radius = spaceship.size * 0.75f,  // Adjust size of the rounded tail
            center = Offset(
                shipPosition.x,
                shipPosition.y + spaceship.size
            )  // Position at the base of the ship
        )
    }
}
}
// Drawing the asteroid with dynamic size from GameState
fun DrawScope.drawAsteroid(asteroid: Asteroid) {
    val asteroidPosition = Offset(asteroid.x, asteroid.y)

    drawCircle(
        color = Color.Gray,
        radius = asteroid.size,
        center = asteroidPosition
    )
}

// Drawing bullets
fun DrawScope.drawBullet(bullet: Bullet) {
    val bulletPosition = Offset(bullet.x, bullet.y)
    drawCircle(
        color = Color.Yellow,
        radius = bullet.size,
        center = bulletPosition
    )
}