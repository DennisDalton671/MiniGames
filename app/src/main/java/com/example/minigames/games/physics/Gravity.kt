package com.example.minigames.games.physics

class Gravity(
    private val gravityStrength: Float = 9.8f,  // Default gravity strength
    private val maxFallSpeed: Float = 50f       // Cap the fall speed
) {
    private var velocityY: Float = 0f   // The vertical speed of the object

    // Function to apply gravity and return the new Y position
    fun applyGravity(currentY: Float, deltaTime: Float): Float {
        velocityY += gravityStrength * deltaTime  // Increase downward velocity based on gravity
        if (velocityY > maxFallSpeed) {
            velocityY = maxFallSpeed  // Cap the fall speed
        }

        // Return the updated Y position
        return currentY + velocityY * deltaTime
    }

    // Function to apply a jump or flap, which resets the velocity upwards
    fun jump(jumpStrength: Float) {
        velocityY = -jumpStrength  // Apply upward force, negative because up is opposite to gravity
    }

    // Function to reset the velocity (useful after death or game reset)
    fun reset() {
        velocityY = 0f
    }
}