package com.example.minigames.ui.GameUI.Components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SwipeToControlText(
    neonColor: Color,
                       ) {
    // Pulse animation for the neon green effect on the text
    val infiniteTransition = rememberInfiniteTransition()
    val neonPulse by infiniteTransition.animateFloat(
        initialValue = 0.7f,
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

    val neon = adjustBrightness(neonColor, neonPulse)

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Swipe text with a neon glow and pulsing effect
        Text(
            text = "Swipe to control",
            fontSize = 22.sp, // Bigger text for visibility
            fontFamily = FontFamily.Monospace, // Blocky retro style
            fontWeight = FontWeight.Bold,
            color = neon, // Neon green text
            textAlign = TextAlign.Center,
            modifier = Modifier
                .neonGlowEffectSwipeText(neonColor) // Apply glow effect to the text
        )
    }
}

// Custom Modifier for Neon Glow Effect on the Swipe Text
fun Modifier.neonGlowEffectSwipeText(color: Color): Modifier = this.then(
    Modifier.drawBehind {
        val textGlowColor = color.copy(alpha = 0.4f) // Subtle glow
        val glowRadius = 12.dp.toPx() // Glow radius for bigger glow

        // Draw a soft glow behind the text
        drawRect(
            color = textGlowColor,
            topLeft = Offset(-glowRadius / 2, -glowRadius / 2),
            size = Size(size.width + glowRadius, size.height + glowRadius)
        )
    }
)