package com.example.minigames.games.FlappyBird

data class Bird(
    var x: Float,       // X position of the bird
    var y: Float,       // Y position of the bird
    var velocity: Float = 0f, // Current vertical velocity
    val width: Float,   // Width of the bird (scaled with canvas)
    val height: Float,  // Height of the bird (scaled with canvas)
    val flapStrength: Float = -30f,  // Strength of the flap (upward velocity)
    val gravity: Float = 2.0f        // Gravity constant
) {

    // Update the bird's position based on gravity
    fun updatePosition() {
        velocity += gravity
        y += velocity
    }

    // Apply upward force when the bird flaps
    fun flap() {
        velocity = flapStrength
    }

    // Check if the bird has hit the ground or ceiling
    fun checkOutOfBounds(canvasHeight: Float): Boolean {
        return y <= 0 || y >= canvasHeight - height
    }
}