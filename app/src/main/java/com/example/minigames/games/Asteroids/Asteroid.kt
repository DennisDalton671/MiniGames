package com.example.minigames.games.Asteroids

data class Asteroid(
    var x: Float,       // Position X
    var y: Float,       // Position Y
    var size: Float,    // Size (big, medium, small)
    var velocityX: Float, // Velocity in X direction
    var velocityY: Float  // Velocity in Y direction
)

// Asteroid splitting logic
fun splitAsteroid(asteroid: Asteroid): List<Asteroid> {
    // Create smaller asteroids if the current asteroid is not the smallest size
    val newAsteroids = mutableListOf<Asteroid>()
    if (asteroid.size > 20f) {  // Only split if the asteroid is big enough
        // Create two smaller asteroids with random velocities
        val smallerSize = asteroid.size / 2f
        newAsteroids.add(Asteroid(asteroid.x, asteroid.y, smallerSize, randomVelocity(), randomVelocity()))
        newAsteroids.add(Asteroid(asteroid.x, asteroid.y, smallerSize, -randomVelocity(), -randomVelocity()))
    }
    return newAsteroids
}

fun randomVelocity(): Float {
    return (1..5).random().toFloat()
}

// Update asteroid positions
fun updateAsteroids(asteroids: MutableList<Asteroid>, canvasWidth: Float, canvasHeight: Float) {
    asteroids.forEach { asteroid ->
        asteroid.x += asteroid.velocityX
        asteroid.y += asteroid.velocityY

        // Screen wrapping logic
        if (asteroid.x < 0) asteroid.x = canvasWidth
        if (asteroid.x > canvasWidth) asteroid.x = 0f
        if (asteroid.y < 0) asteroid.y = canvasHeight
        if (asteroid.y > canvasHeight) asteroid.y = 0f
    }
}