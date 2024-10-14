package com.example.minigames.games.Asteroids

import android.util.Log
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

data class Spaceship(
    var x: Float,
    var y: Float,
    var size: Float = 20f,
    var direction: Float,
    var velocityX: Float = 0f,
    var velocityY: Float = 0f,
    var maxSpeed: Float = 10f,
    var invincibilityTimer: Long = 0L, // Invincibility timer
    var isVisible: Boolean = true // Flag to control visibility
)


fun updateSpaceshipMovement(spaceship: Spaceship, speedMultiplier: Float, angle: Float) {
    // Update the direction of the spaceship based on input

    spaceship.direction = angle

    // Calculate the velocity components based on the angle
    val radianAngle = Math.toRadians(angle.toDouble())
    spaceship.velocityX = (speedMultiplier * cos(radianAngle)).toFloat()
    spaceship.velocityY = (speedMultiplier * sin(radianAngle)).toFloat()

    Log.d("Spaceship Movement", "VelocityX: ${spaceship.velocityX}, VelocityY: ${spaceship.velocityY}")

    // Cap the speed to avoid excessive movement
    spaceship.velocityX = spaceship.velocityX.coerceIn(-spaceship.maxSpeed, spaceship.maxSpeed)
    spaceship.velocityY = spaceship.velocityY.coerceIn(-spaceship.maxSpeed, spaceship.maxSpeed)

    // Update the position of the spaceship
    spaceship.x += spaceship.velocityX
    spaceship.y += spaceship.velocityY
    //Log.d("Spaceship", "Position: (${spaceship.x}, ${spaceship.y}), Velocity: (${spaceship.velocityX}, ${spaceship.velocityY})")
}