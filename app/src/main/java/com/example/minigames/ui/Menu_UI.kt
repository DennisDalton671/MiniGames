package com.example.minigames.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.minigames.gamestate.AppViewModel
import com.example.minigames.ui.navigation.Screen
import com.example.minigames.ui.theme.AppTheme

//private lateinit var appState: AppState

var first = true

@Composable
fun Menu_UI(navController: NavController, viewModel: AppViewModel) {
    // Use the current theme directly from AppState
    val isDarkTheme = viewModel.isDarkTheme

    // Start the music when entering the Main Menu
    if (first) {
        LaunchedEffect(Unit) {
            viewModel.startMenuMusic()
        }
        first = false
    }

    AppTheme(isDarkTheme = isDarkTheme) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.background)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Main menu UI content here, such as buttons and title
            ArcadeMenuWithDPadAndOffsetButtons(navController, viewModel)
        }
    }
}

@Composable
fun ArcadeMenuWithDPadAndOffsetButtons(navController: NavController, viewModel: AppViewModel) {
    val infiniteTransition = rememberInfiniteTransition()

    // Neon pulsing effect for all elements
    val neonPulse by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background), // Dark gray background
        contentAlignment = Alignment.TopEnd // Align content at the top-right
    ) {
        // Settings Button in the top-right corner
        IconButton(
            onClick = {
                navController.navigate(Screen.Settings.route) // Navigate to Settings screen
            },
            modifier = Modifier
                .padding(16.dp)
                .size(48.dp)
                .background(AppTheme.colors.contentBackground, CircleShape)
                .border(4.dp, AppTheme.colors.neonRed, CircleShape) // Border for the frame
        ) {
            Icon(
                imageVector = Icons.Default.Settings, // Built-in settings icon
                contentDescription = "Settings",
                tint = AppTheme.colors.dPad, // Icon color, customizable in your theme
                modifier = Modifier.size(24.dp) // Size of the gear icon
            )
        }

        // Main Column for the arcade screen and controls
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = "Mini Games",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.textColor,
                modifier = Modifier.padding(top = 40.dp, bottom = 16.dp)
            )

            // Neon border box with game buttons (adjusted height to take up more space)
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f) // Increase width
                    .fillMaxHeight(0.75f) // Increase height to take more screen space
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                AppTheme.colors.background,
                                AppTheme.colors.contentBackground
                            )
                        )
                    ),
            ) {
                NeonBorder(
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(1f), // Ensure the neon border is on top of the black background
                    neonPulse = neonPulse
                ) {
                    // Box that contains the game selection buttons
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp)) // Rounded corners inside the neon border
                            .background(Color.Transparent), // Make the content background transparent
                        contentAlignment = Alignment.Center
                    ) {
                        // Column with buttons for game selection
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp) // Space between buttons
                        ) {
                            // Asteroids Button
                            Button(
                                onClick = {
                                    viewModel.startNewAsteroidsGame()
                                    navController.navigate(Screen.AsteroidsMenu.route)
                                },
                                modifier = Modifier
                                    .width(200.dp)
                                    .height(50.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            ) {
                                Text(text = "Space Rocks", color = AppTheme.colors.textColor, fontSize = 18.sp)
                            }

                            // Flappy Bird Button
                            Button(
                                onClick = {
                                    viewModel.startNewFlappyBirdGame()
                                    navController.navigate(Screen.FlappyBirdMenu.route)
                                },
                                modifier = Modifier
                                    .width(200.dp)
                                    .height(50.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            ) {
                                Text(text = "Wing Dash", color = AppTheme.colors.textColor, fontSize = 18.sp)
                            }

                            // Snake Button
                            Button(
                                onClick = {
                                    viewModel.startNewSnakeGame()
                                    navController.navigate(Screen.SnakeMenu.route)
                                },
                                modifier = Modifier
                                    .width(200.dp)
                                    .height(50.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            ) {
                                Text(text = "Neon Serpent", color = AppTheme.colors.textColor, fontSize = 18.sp)
                            }

                            /*
                            // Tiny Wings Button
                            Button(
                                onClick = {
                                    viewModel.startNewTinyWingsGame()
                                    navController.navigate(Screen.TinyWingsMenu.route)
                                },
                                modifier = Modifier
                                    .width(200.dp)
                                    .height(50.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            ) {
                                Text(text = "Tiny Wings (WIP)", color = AppTheme.colors.textColor, fontSize = 18.sp)
                            } */
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Row for the D-pad and face buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),  // Outer padding for the whole row
                horizontalArrangement = Arrangement.SpaceBetween,  // Add space between the D-pad and buttons
                verticalAlignment = Alignment.CenterVertically
            ) {
                // D-Pad on the Left
                CustomDPad()

                Spacer(modifier = Modifier.width(25.dp)) // Spacer to add more distance between D-pad and buttons

                // A and B Buttons on the Right (offset)
                Box(
                    modifier = Modifier
                        .offset(x = (-20).dp, y = (-10).dp) // Diagonal offset
                ) {
                    OffsetArcadeButtons()
                }
            }
        }
    }
}





@Composable
fun CustomDPad() {
    Box(
        modifier = Modifier.size(130.dp), // D-pad size
        contentAlignment = Alignment.Center
    ) {
        // Vertical Rectangle for the D-pad
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(110.dp)
                .background(AppTheme.colors.dPad)
        )

        // Horizontal Rectangle for the D-pad
        Box(
            modifier = Modifier
                .width(110.dp)
                .height(40.dp)
                .background(AppTheme.colors.dPad)
        )

        // Up arrow
        DrawTriangle(
            direction = Direction.UP,
            modifier = Modifier
                .size(20.dp)  // Adjusted size slightly smaller
                .align(Alignment.TopCenter)
                .offset(y = (5).dp) // Minor offset tweak to fit inside the D-pad
        )

        // Down arrow
        DrawTriangle(
            direction = Direction.DOWN,
            modifier = Modifier
                .size(20.dp)  // Adjusted size slightly smaller
                .align(Alignment.BottomCenter)
                .offset(y = (-5).dp) // Minor offset tweak to fit inside the D-pad
        )

        // Left arrow
        DrawTriangle(
            direction = Direction.LEFT,
            modifier = Modifier
                .size(20.dp)  // Adjusted size slightly smaller
                .align(Alignment.CenterStart)
                .offset(x = (15).dp) // Minor offset tweak to fit inside the D-pad
        )

        // Right arrow
        DrawTriangle(
            direction = Direction.RIGHT,
            modifier = Modifier
                .size(20.dp)  // Adjusted size slightly smaller
                .align(Alignment.CenterEnd)
                .offset(x = (-15).dp) // Minor offset tweak to fit inside the D-pad
        )
    }
}

@Composable
fun DrawTriangle(direction: Direction, modifier: Modifier = Modifier) {
    val density = LocalDensity.current
    Canvas(modifier = modifier) {
        val path = Path().apply {
            when (direction) {
                Direction.UP -> {
                    moveTo(size.width / 2, 0f)
                    lineTo(0f, size.height)
                    lineTo(size.width, size.height)
                }
                Direction.DOWN -> {
                    moveTo(size.width / 2, size.height)
                    lineTo(0f, 0f)
                    lineTo(size.width, 0f)
                }
                Direction.LEFT -> {
                    moveTo(0f, size.height / 2)
                    lineTo(size.width, 0f)
                    lineTo(size.width, size.height)
                }
                Direction.RIGHT -> {
                    moveTo(size.width, size.height / 2)
                    lineTo(0f, 0f)
                    lineTo(0f, size.height)
                }
            }
            close()
        }
        drawPath(path, SolidColor(Color.White))
    }
}


enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

@Composable
fun OffsetArcadeButtons() {
    Row(
        modifier = Modifier.offset(x = (-10).dp, y = (20).dp), // Adjusted offsets to bring closer to D-pad
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Button (B)
        ArcadeButton(label = "B", color = AppTheme.colors.buttonColor)

        Spacer(modifier = Modifier.width(16.dp)) // Smaller space between buttons

        // Right Button (A) with a slight upward offset
        Box(modifier = Modifier.offset(y = (-20).dp)) {
            ArcadeButton(label = "A", color = AppTheme.colors.buttonColor) // Same color as Button B
        }
    }
}
@Composable
fun ArcadeButton(label: String, color: Color) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(50.dp) // Button size
            .clip(CircleShape)
            .background(color)
            .border(4.dp, Color.White, CircleShape) // Button border
    ) {
        Text(text = label, color = AppTheme.colors.textColor, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun NeonBorder(
    modifier: Modifier = Modifier,
    neonPulse: Float = 1f, // Ensure neonPulse is defined as a parameter
    content: @Composable BoxScope.() -> Unit // This allows composable content inside the NeonBorder
) {
    val colors = listOf(
        AppTheme.colors.neonRed,
        AppTheme.colors.neonOrange,
        AppTheme.colors.neonYellow,
        AppTheme.colors.neonGreen,
        AppTheme.colors.neonCyan
    )

    Box(modifier = modifier) {
        // Multiple layers of borders, each with slightly different padding
        colors.forEachIndexed { index, color ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding((index * 5).dp) // Adjust the padding for each layer
                    .border(4.dp, adjustBrightness(color, neonPulse), RectangleShape)
            )
        }

        // This allows the inner content to be placed inside the border
        Box(modifier = Modifier.fillMaxSize()) {
            content() // Display the content inside the NeonBorder
        }
    }
}

fun adjustBrightness(color: Color, factor: Float): Color {
    return Color(
        red = (color.red * factor).coerceIn(0f, 1f),
        green = (color.green * factor).coerceIn(0f, 1f),
        blue = (color.blue * factor).coerceIn(0f, 1f),
        alpha = color.alpha
    )
}