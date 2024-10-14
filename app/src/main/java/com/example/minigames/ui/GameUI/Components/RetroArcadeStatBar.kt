package com.example.minigames.ui.GameUI.Components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RetroArcadeStatBar(
    score: Int,
    time: String,
    lives: Int,
    Score: String,
    Lives: String,
    textColor: Color
) {
    // Pulse animation for the neon green effect on the border
    val infiniteTransition = rememberInfiniteTransition()
    val neonPulse by infiniteTransition.animateFloat(
        initialValue = 0.7f, // Make the glow a little more noticeable
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Function to adjust color brightness for neon glow effect
    fun adjustBrightness(color: Color, factor: Float): Color {
        return Color(
            red = (color.red * factor).coerceIn(0f, 1f),
            green = (color.green * factor).coerceIn(0f, 1f),
            blue = (color.blue * factor).coerceIn(0f, 1f)
        )
    }

    val neonGreen = adjustBrightness(Color.Green, neonPulse)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.12f) // Slightly taller for more arcade feel
            .border(BorderStroke(8.dp, textColor)) // Neon pulsing border
            .background(Color.Black) // Ensure full black background
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        // Text with a retro arcade style
        Text(
            text = "Stats: $Score $score | Time $time | $Lives $lives",
            fontSize = 20.sp, // Slightly larger text
            fontFamily = FontFamily.Monospace, // Retro, blocky font
            fontWeight = FontWeight.Bold,
            color = textColor, // Neon green color for the text
            textAlign = TextAlign.Center,
            modifier = Modifier
                .background(Color.Black) // Ensure the background around text is black
        )
    }
}