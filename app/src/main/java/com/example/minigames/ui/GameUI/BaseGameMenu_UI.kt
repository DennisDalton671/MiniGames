package com.example.minigames.ui.GameUI

import android.net.Uri
import android.widget.VideoView
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.minigames.ui.theme.AppTheme
import com.example.minigames.R

@Composable
fun BaseGameUI(
    gameTitle: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    gameDescription: String = "",
    textColor: Color, // Passed from specific game
    borderColor: Color, // Passed from specific game
    videoResource: Int = R.raw.snake_quest// Pass the video resource ID as a parameter
) {
    val infiniteTransition = rememberInfiniteTransition()
    val backgroundColor = AppTheme.colors.background
    val contentBackgroundColor = AppTheme.colors.contentBackground

    val context = LocalContext.current


    // Neon pulsing effect for all elements
    val neonPulse by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Background animation (shifting pixel grid)
    val offsetX by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Function to adjust color brightness by scaling the RGB values
    fun adjustBrightness(color: Color, factor: Float): Color {
        return Color(
            red = (color.red * factor).coerceIn(0f, 1f),
            green = (color.green * factor).coerceIn(0f, 1f),
            blue = (color.blue * factor).coerceIn(0f, 1f)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background) // Theme background color
    ) {
        // Moving background (shifting pixelated effect)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val pixelSize = 20.dp.toPx()
            val width = size.width
            val height = size.height

            val columns = (width / pixelSize).toInt()
            val rows = (height / pixelSize).toInt()

            for (x in 0 until columns) {
                for (y in 0 until rows) {
                    drawRect(
                        color = if ((x + y) % 2 == 0) backgroundColor else contentBackgroundColor,
                        topLeft = Offset(
                            x * pixelSize + offsetX % pixelSize,
                            y * pixelSize
                        ),
                        size = Size(pixelSize, pixelSize)
                    )
                }
            }
        }

        // Neon borders with AppTheme neon colors
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)
                .border(8.dp, adjustBrightness(AppTheme.colors.neonRed, neonPulse), shape = RectangleShape)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .border(2.dp, AppTheme.colors.background, shape = RectangleShape) // Specific game border color
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .border(8.dp, adjustBrightness(AppTheme.colors.neonOrange, neonPulse), shape = RectangleShape)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp)
                .border(2.dp, AppTheme.colors.background, shape = RectangleShape) // Specific game border color
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .border(8.dp, adjustBrightness(AppTheme.colors.neonYellow, neonPulse), shape = RectangleShape)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp)
                .border(2.dp, AppTheme.colors.background, shape = RectangleShape) // Specific game border color
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
                .border(8.dp, adjustBrightness(AppTheme.colors.neonGreen, neonPulse), shape = RectangleShape)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(38.dp)
                .border(2.dp, AppTheme.colors.background, shape = RectangleShape) // Specific game border color
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp)
                .border(8.dp, adjustBrightness(AppTheme.colors.neonCyan, neonPulse), shape = RectangleShape)
        )

        // Scrollable column for content with padding to avoid borders
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // Makes the content scrollable
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly // Equal spacing
        ) {
            // Game title with neon border
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .border(4.dp, adjustBrightness(textColor, neonPulse), shape = RoundedCornerShape(16.dp)) // Specific game text color
                    .background(AppTheme.colors.contentBackground, shape = RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = gameTitle,
                    fontSize = 32.sp,
                    color = textColor, // Specific game text color
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(180.dp)
                    .border(
                        width = 4.dp,
                        color = adjustBrightness(textColor, neonPulse), // Specific game text color
                        shape = RoundedCornerShape(16.dp)
                    )
                    .background(AppTheme.colors.background, shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                // val context = LocalContext.current

                AndroidView(
                    factory = {
                        val videoView = VideoView(context).apply {
                            // Set the video URI
                            val videoUri = Uri.parse("android.resource://${context.packageName}/$videoResource")
                            setVideoURI(videoUri)

                            // Set the video to loop
                            setOnCompletionListener {
                                it.start() // Restart the video once it completes
                            }

                            // Start the video
                            start()
                        }

                        videoView
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp)), // Ensure the VideoView is properly clipped
                    update = {
                        // Logic for interacting with VideoView during recomposition (if needed)
                    },
                    onRelease = {
                        // Release resources properly when the Composable is disposed
                        it.stopPlayback()
                        it.suspend()
                    }
                )
            }
            // Options buttons with neon border
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                options.forEach { option ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .border(4.dp, adjustBrightness(textColor, neonPulse), shape = RoundedCornerShape(16.dp)) // Specific game text color
                            .background(AppTheme.colors.contentBackground, shape = RoundedCornerShape(16.dp))
                            .clickable { onOptionSelected(option) }
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = option,
                            fontSize = 18.sp,
                            color = textColor, // Specific game text color
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }

            // Game Description with neon border
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .border(
                        width = 4.dp,
                        color = adjustBrightness(textColor, neonPulse), // Specific game text color
                        shape = RoundedCornerShape(16.dp)
                    )
                    .background(AppTheme.colors.contentBackground, shape = RoundedCornerShape(16.dp))
                    .padding(10.dp)
            ) {
                Text(
                    text = gameDescription,
                    color = textColor, // Specific game text color
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}


