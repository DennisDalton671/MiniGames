package com.example.minigames.games.Asteroids

import kotlin.math.cos
import kotlin.math.sin

data class Bullet(
    var x: Float,       // Position X
    var y: Float,       // Position Y
    var size: Float = 5f,  // Bullet size
    var velocityX: Float, // Velocity in X direction
    var velocityY: Float, // Velocity in Y direction
    var lifetime: Int = 60 // How long the bullet lasts (in frames)
)


fun updateBullets(bullets: MutableList<Bullet>, canvasWidth: Float, canvasHeight: Float) {
    bullets.forEach { bullet ->
        bullet.x += bullet.velocityX
        bullet.y += bullet.velocityY
        bullet.lifetime--

        // Screen wrapping logic for bullets
        if (bullet.x < 0) bullet.x = canvasWidth
        if (bullet.x > canvasWidth) bullet.x = 0f
        if (bullet.y < 0) bullet.y = canvasHeight
        if (bullet.y > canvasHeight) bullet.y = 0f
    }

    // Remove bullets when they have exceeded their lifetime
    bullets.removeAll { bullet -> bullet.lifetime <= 0 }
}
