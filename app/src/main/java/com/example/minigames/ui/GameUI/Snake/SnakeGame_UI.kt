package com.example.minigames.ui.GameUI.Snake

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.minigames.gamestate.game.SnakeGameState
import com.example.minigames.ui.theme.AppTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.minigames.gamestate.AppViewModel
import com.example.minigames.ui.GameUI.Components.RetroArcadeStatBar
import com.example.minigames.ui.GameUI.Components.SwipeToControlText
import kotlinx.coroutines.delay
import kotlin.math.abs

enum class BlockType {
    EMPTY,
    SNAKE,
    FOOD
}

@Composable
fun RenderBlock(blockType: BlockType) {
    // Adjusting the block colors, making the background a darker gray
    val blockColor = when (blockType) {
        BlockType.EMPTY -> Color(0xFF2E2E2E) // Slightly darker gray
        BlockType.SNAKE -> Color.Green
        BlockType.FOOD -> Color.Red
    }

    // Pulse animation for the food to make it stand out
    val infiniteTransition = rememberInfiniteTransition()
    val pulseValue by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.0f, // Ensure the maximum value does not exceed 1.0
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val foodColor = if (blockType == BlockType.FOOD) {
        AppTheme.colors.neonRed.copy(alpha = pulseValue.coerceIn(0.0f, 1.0f)) // Pulsing effect for food
    } else {
        blockColor
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(foodColor) // Set the block color
            .then(
                // Add classic grid effect only for snake blocks
                if (blockType == BlockType.SNAKE) {
                    Modifier.border(1.dp, Color.Black) // Classic border for snake blocks
                } else {
                    Modifier
                }
            )
    )
}

@Composable
fun SnakeGame_UI(navController: NavController, viewModel: AppViewModel) {
    // Create SnakeGameState in the ViewModel
    var snakeGameState by remember { mutableStateOf(SnakeGameState(20, 20)) } // Initial game state
    var hasInteracted by remember { mutableStateOf(false) } // Track interaction
    var gameStarted by remember { mutableStateOf(false) } // Track if the game has started

    // Pulse animation for the neon effect (green borders)
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

    val neonGreen = adjustBrightness(Color.Green, neonPulse) // Pulsing neon green
    val darkerGreenColor = Color(0xFF003300) // Dark green for the swipe zone

    // Game loop to move the snake
    LaunchedEffect(gameStarted) {
        if (gameStarted) {
            while (!snakeGameState.isSGameOver) {
                snakeGameState.moveSnake() // Move snake through GameState
                delay(150L) // Control speed of the game (150ms per move)
            }

            // Handle game over logic and reset
            if (snakeGameState.isSGameOver) {

                // Delay a little to allow user to see the game over state before resetting
                delay(1000L)

                // Reset game state and flags
                //snakeGameState = SnakeGameState(20, 20) // Reset the game state
                snakeGameState.resetGameS()
                hasInteracted = false // Reset interaction flag
                gameStarted = false // Reset game started flag
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
    ) {
        // Top section for stats (e.g., food eaten, time, death counter)
        RetroArcadeStatBar(
            score = snakeGameState.score, // Use actual score from game state
            time = snakeGameState.getElapsedTimeFormatted(),
            lives = snakeGameState.retries,
            Score = "Food",
            Lives = "Deaths",
            textColor = AppTheme.colors.neonGreen
        )

        // Middle section for game visuals with grid
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Adjusts to take up remaining space
                .border(BorderStroke(2.dp, neonGreen)) // Smaller border size
                .background(AppTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            // Use the dynamic game grid from GameState
            SnakeGrid(snakeGameState.grid)
        }

        // Bottom section for gesture controls (reduced to 35% of the screen)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.40f) // Adjusted height for swipe section
                .border(BorderStroke(2.dp, neonGreen)) // Smaller border size
                .background(darkerGreenColor) // Dark green background for swipe zone
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        val (dx, dy) = dragAmount
                        change.consume()
                        hasInteracted = true // Set to true once user interacts

                        // Only start the game loop after the first interaction
                        if (!gameStarted) {
                            snakeGameState.startNewGame()  // Reset stats and start game
                            gameStarted = true
                        }

                        // Determine swipe direction based on delta
                        when {
                            abs(dx) > abs(dy) && dx > 0 -> {
                                println("Swiped right")
                                snakeGameState.changeDirection(SnakeGameState.Direction.RIGHT) // Swipe right
                            }

                            abs(dx) > abs(dy) && dx < 0 -> {
                                println("Swiped left")
                                snakeGameState.changeDirection(SnakeGameState.Direction.LEFT)  // Swipe left
                            }

                            abs(dy) > abs(dx) && dy > 0 -> {
                                println("Swiped down")
                                snakeGameState.changeDirection(SnakeGameState.Direction.DOWN)  // Swipe down
                            }

                            abs(dy) > abs(dx) && dy < 0 -> {
                                println("Swiped up")
                                snakeGameState.changeDirection(SnakeGameState.Direction.UP)    // Swipe up
                            }
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            // Only show the swipe text if the user hasn't interacted yet
            if (!hasInteracted) {
                SwipeToControlText(neonColor = AppTheme.colors.neonGreen) // Show flashy swipe text if no interaction
            }
        }
    }
}

@Composable
fun SnakeGrid(grid: Array<Array<BlockType>>) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        for (row in grid) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // Ensures rows take equal vertical space
                horizontalArrangement = Arrangement.Start
            ) {
                for (block in row) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        RenderBlock(block) // Renders each block (Empty, Snake, Food)
                    }
                }
            }
        }
    }
}

// Custom Modifier for Neon Glow Effect on the Text
fun Modifier.neonGlowEffect(): Modifier = this.then(
    Modifier.drawBehind {
        val textGlowColor = Color(0xFF00FF00).copy(alpha = 0.4f) // Adjust alpha for a more defined glow
        val glowRadius = 8.dp.toPx() // Increased glow radius

        // Draw a soft glow behind the text, only within a small area
        drawRect(
            color = textGlowColor,
            topLeft = Offset(-glowRadius / 2, -glowRadius / 2),
            size = Size(size.width + glowRadius, size.height + glowRadius)
        )
    }
)

