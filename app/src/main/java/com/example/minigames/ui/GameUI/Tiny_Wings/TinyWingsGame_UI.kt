package com.example.minigames.ui.GameUI.Tiny_Wings

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.mutableStateMapOf
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.minigames.games.TinyWings.Hill
import com.example.minigames.games.TinyWings.HillGeometry
import com.example.minigames.gamestate.AppViewModel
import com.example.minigames.gamestate.game.FlappyBirdGameState
import com.example.minigames.gamestate.game.TinyWingsGameState
import com.example.minigames.ui.theme.AppTheme
import com.example.minigames.ui.theme.TinyBird
import kotlinx.coroutines.delay
import kotlin.math.sin
import kotlin.random.Random
import androidx.compose.ui.graphics.Path as ComposePath

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun TinyWingsGame_UI(navController: NavController, viewModel: AppViewModel) {

    if (viewModel.gameState is TinyWingsGameState) {
        val tinyWingsGameState = viewModel.gameState as TinyWingsGameState

        var gameStarted by remember { mutableStateOf(false) }
        var isHolding by remember { mutableStateOf(false) }
        var canvasInitialized by remember { mutableStateOf(false) }

        // Cache hills points to avoid excessive generation
        val hillPointsCache = remember { mutableStateMapOf<Hill, List<Pair<Float, Float>>>() }

        // Access activity to set orientation
        val activity = LocalContext.current as? ComponentActivity
        val tinyBird = AppTheme.colors.tinyBird
        val tinySlope = AppTheme.colors.tinySlopes

        // Launch game loop with 60 FPS
        LaunchedEffect(gameStarted) {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            if (gameStarted) {
                while (tinyWingsGameState.isRunning()) {
                    tinyWingsGameState.update(0.016f)
                    if (isHolding) {
                        tinyWingsGameState.performDive()
                    }
                    delay(16L)
                }
            }
        }

        BackHandler(enabled = true) {
            navController.popBackStack()
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.tinyBackground)
                .windowInsetsPadding(WindowInsets.systemBars)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            if (!gameStarted) {
                                gameStarted = true
                            } else {
                                isHolding = true
                                tryAwaitRelease()
                                isHolding = false
                                tinyWingsGameState.stopDive()
                            }
                        }
                    )
                }
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height

                    // Initialize canvas dimensions only once
                    if (!canvasInitialized) {
                        Log.e("Canvas", "Height: $canvasHeight + Width: $canvasWidth")
                        tinyWingsGameState.setCanvasSize(canvasWidth, canvasHeight)
                        canvasInitialized = true
                    }

                    // Draw the bird
                    drawBird(
                        positionX = tinyWingsGameState.birdPositionX,
                        positionY = tinyWingsGameState.birdPositionY,
                        size = 30f,
                        birdColor = tinyBird
                    )

                    // Draw the hills using cached points
                    tinyWingsGameState.hills.forEach { hill ->
                        val hillPoints = hillPointsCache.getOrPut(hill) {
                            val hillGeometry = HillGeometry(scaleFactor = 1.0f)
                            hillGeometry.generateHillPoints(listOf(hill), canvasWidth, canvasHeight)
                        }
                        drawSplineHills(hillPoints, tinySlope)
                    }
                }
            }

            BottomBar(distance = 0, retries = 0, points = 0)
        }
    }
}

@Composable
fun BottomBar(distance: Int, retries: Int, points: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(25.dp)
            .background(Color.DarkGray)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Distance: $distance", color = Color.White)
        Text(text = "Retries: $retries", color = Color.White)
        Text(text = "Points: $points", color = Color.White)
    }
}

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

fun DrawScope.drawSplineHills(
    hillPoints: List<Pair<Float, Float>>,
    slopeColor: Color
) {
    val path = ComposePath()

    if (hillPoints.isNotEmpty()) {
        // Start at the first hill point
        val startX = hillPoints.first().first
        val startY = hillPoints.first().second
        path.moveTo(startX, startY)
        Log.d("drawSplineHills", "Start Point - x: $startX, y: $startY")

        // Draw each hill segment as a quadratic Bézier curve
        for (i in 1 until hillPoints.size step 2) {
            val controlPoint = hillPoints[i]
            val endPoint = hillPoints.getOrNull(i + 1) ?: controlPoint

            Log.d("drawSplineHills", "Control Point - x: ${controlPoint.first}, y: ${controlPoint.second}")
            Log.d("drawSplineHills", "End Point - x: ${endPoint.first}, y: ${endPoint.second}")

            path.quadraticBezierTo(
                controlPoint.first, controlPoint.second,  // Control point
                endPoint.first, endPoint.second           // End point
            )
        }

        // Ensure path covers the width properly and closes along the bottom edge
        path.lineTo(hillPoints.last().first, size.height)  // Bottom right of last hill point
        path.lineTo(hillPoints.first().first, size.height) // Bottom left of first hill point
        path.close()
    }

    // Draw the complete path with the specified slope color
    drawPath(path = path, color = slopeColor)

    // Draw debug circles at each hill point to visualize key locations
    hillPoints.forEach { (x, y) ->
        Log.d("drawSplineHills", "Debug Point - x: $x, y: $y")
        drawCircle(color = Color.Red, radius = 5f, center = Offset(x, y))
    }
}
// Utility function to linearly interpolate between two values
fun lerp(start: Float, end: Float, fraction: Float): Float {
    return start + fraction * (end - start)
}