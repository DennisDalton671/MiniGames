package com.example.minigames.ui.GameUI.Tiny_Wings

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.minigames.games.TinyWings.Hill
import com.example.minigames.gamestate.AppViewModel
import com.example.minigames.gamestate.game.TinyWingsGameState
import com.example.minigames.ui.theme.AppTheme
import com.example.minigames.ui.theme.TinyBird
import kotlin.math.sin
import kotlin.random.Random
import androidx.compose.ui.graphics.Path as ComposePath

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun TinyWingsGame_UI(navController: NavController, viewModel: AppViewModel) {

    if (viewModel.gameState is TinyWingsGameState) {

        val activity = LocalContext.current as? ComponentActivity
        var isLandscapeSet by remember { mutableStateOf(false) }

        // Set to landscape only once
        LaunchedEffect(Unit) {
            if (!isLandscapeSet) {
                activity?.window?.setDecorFitsSystemWindows(false)
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                isLandscapeSet = true
            }
        }

        // Listen for back navigation to set portrait mode
        BackHandler(enabled = true) {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            navController.popBackStack() // Navigate back after setting orientation
        }

        // Define the base UI layout
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.tinyBackground)
                .windowInsetsPadding(WindowInsets.systemBars) // Fill the entire screen
        ) {
            // Game area taking up all available space above the BottomBar
            Box(
                modifier = Modifier
                    .weight(1f)  // The game area takes up remaining space
                    .fillMaxWidth()
            ) {
                var birdYPosition by remember { mutableStateOf(0.1f) } // Y position as a fraction of screen height

                // Example hills
                val testHills = listOf(
                    Hill(startY = 0.3f, midY = 0.5f, endY = 0.2f, isHill = true),   // Tall hill
                    Hill(startY = 0.2f, midY = 0.1f, endY = 0.25f, isHill = false), // Shallow dip
                    Hill(startY = 0.25f, midY = 0.4f, endY = 0.3f, isHill = true),  // Moderate hill
                    Hill(startY = 0.3f, midY = 0.15f, endY = 0.35f, isHill = false), // Deeper dip
                    Hill(startY = 0.35f, midY = 0.6f, endY = 0.3f, isHill = true),  // Tall steep hill
                    Hill(startY = 0.3f, midY = 0.1f, endY = 0.3f, isHill = false),  // Deep dip
                    Hill(startY = 0.3f, midY = 0.45f, endY = 0.25f, isHill = true), // Slightly taller hill
                    Hill(startY = 0.25f, midY = 0.2f, endY = 0.3f, isHill = false), // Smooth dip
                    Hill(startY = 0.3f, midY = 0.5f, endY = 0.25f, isHill = true),  // Large hill again
                    Hill(startY = 0.25f, midY = 0.15f, endY = 0.3f, isHill = false) // Moderate dip
                )

                ScalingCameraGameArea(
                    birdYPosition = birdYPosition,
                    birdColor = AppTheme.colors.tinyBird,
                    slopeColor = AppTheme.colors.tinySlopes,
                    hills = testHills
                )
            }

            // BottomBar placed below the game area
            BottomBar()
        }
    }
}

@Composable
fun BottomBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(25.dp)
            .background(Color.DarkGray)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Distance: 0", color = Color.White)
        Text(text = "Retries: 0", color = Color.White)
        Text(text = "Points: 0", color = Color.White)
    }
}

//// Function to render the game area with static slopes and a bird
//@Composable
//fun StaticSlopesGameArea(
//    birdColor: Color = Color.Blue,
//    slopeColor: Color = Color.Yellow,
//    hills: List<Hill> // List of predefined peaks from game logic
//) {
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.LightGray)
//    ) {
//        // Draw the static slopes using the peaks list from game logic
//        DrawSplineHills(
//            modifier = Modifier.fillMaxSize(),
//            hills = hills,
//            slopeColor = slopeColor
//        )
//
//        // Draw the bird
//        DrawBird(
//            positionX = 120f,  // Example X position for the bird
//            positionY = 150f,  // Example Y position for the bird
//            size = 30f,        // Example size of the bird
//            birdColor = birdColor
//        )
//    }
//
//}


// Function to draw the bird within the Canvas
fun DrawScope.drawBird(
    positionX: Float,
    positionY: Float,
    size: Float,
    birdColor: Color
) {
    drawCircle(
        color = birdColor,
        radius = size,
        center = Offset(positionX, positionY)
    )
}

// Function to draw hills using paths inside the Canvas, now considering scaling
fun DrawScope.drawSplineHills(
    hills: List<Hill>,
    slopeColor: Color,
    scaleFactor: Float
) {
    val path = ComposePath()

    // Adjust total width based on the scale factor to cover the full screen
    val totalWidth = size.width / scaleFactor
    val totalHeight = size.height

    var currentX = 0f // Start at the leftmost side of the canvas
    val hillWidth = totalWidth / hills.size // Divide the canvas by the number of hills

    // Start the path from the first hill's start point, anchored at the bottom
    path.moveTo(0f, totalHeight * (1 - hills.first().startY) * scaleFactor)

    // Loop through each hill and draw cubic Bézier splines
    for (hill in hills) {
        val startX = currentX
        val endX = currentX + hillWidth
        val midX = startX + hillWidth / 2f // Midpoint in the X direction

        // Adjust Y positions based on the scale factor to keep them anchored to the bottom
        val controlX1 = startX + hillWidth / 2f
        val controlY1 = totalHeight * (1 - hill.midY) * scaleFactor // Adjust Y by scaleFactor

        // Draw the cubic Bézier curve for each segment
        path.quadraticBezierTo(
            controlX1, controlY1,          // Control point
            endX, totalHeight * (1 - hill.endY) * scaleFactor  // End point
        )

        currentX = endX // Update currentX to the end of this hill
    }

    // Close the path to the bottom of the canvas
    path.lineTo(totalWidth, totalHeight) // Anchored at the bottom
    path.lineTo(0f, totalHeight)
    path.close()

    // Draw the path
    drawPath(path = path, color = slopeColor)
}


@Composable
fun ScalingCameraGameArea(
    birdYPosition: Float,  // The bird's Y position (input from game logic)
    birdColor: Color = Color.Cyan,
    slopeColor: Color = Color.Green,
    hills: List<Hill>
) {
    val maxBirdHeight = 1f // The maximum height the bird can fly (as a fraction of screen height)
    val minScale = 0.5f // Minimum zoom-out level
    val maxScale = 1f   // Maximum zoom-in level

    // Calculate the scale factor based on the bird's height
    val scaleFactor = lerp(minScale, maxScale, 1f - birdYPosition / maxBirdHeight)

    Canvas(modifier = Modifier.fillMaxSize()) {
        // Apply scaling based on bird's position
        scale(scaleFactor, pivot = Offset(0f, size.height)) {
            // Draw the hills and anchor them to the bottom
            drawSplineHills(hills = hills, slopeColor = slopeColor, scaleFactor = scaleFactor)

            // Draw the bird
            drawBird(positionX = 100f, positionY = birdYPosition * size.height, size = 30f, birdColor = birdColor)
        }
    }
}
// Utility function to linearly interpolate between two values
fun lerp(start: Float, end: Float, fraction: Float): Float {
    return start + fraction * (end - start)
}