package com.example.minigames.games.Asteroids

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

class AsteroidsGameLogic(
    var canvasWidth: Float = 1440f,
    var canvasHeight: Float = 1526f
) {
    var spaceship = Spaceship(x = canvasWidth / 2, y = canvasHeight / 2, direction = 0f)
    var asteroids = mutableListOf<Asteroid>()
    var bullets = mutableListOf<Bullet>()
    var score = 0
    var lives = 3
    var isGameOver = false
    var angle = 0f
    var speedMultiplier = 0f
    var isRespawning = false
    private var fireCooldownFrames: Int = 0  // Frames left before the ship can shoot again
    private var fireRateFrames: Int = 12     // Initial cooldown in frames (to be calculated dynamically)

    var currentWave = 1 // Track the current wave

    init {
        // Start with some initial asteroids
        initializeAsteroids()
    }

    // Function to calculate the fire rate based on canvas size
    private fun calculateFireRate() {
        val baseRate = 5f // 5 shots per second at 60 FPS
        fireRateFrames = (60 / baseRate).toInt() // Base on a 60 FPS assumption
        println("Fire rate frames calculated as: $fireRateFrames")
    }


    // Function to initialize asteroids for the current wave
    private fun initializeAsteroids() {
        val asteroidCount = 5 + (currentWave - 1) + (0..1).random() // Start with 5 asteroids, add 1-2 more each wave
        for (i in 1..asteroidCount) {
            asteroids.add(
                Asteroid(
                    x = (0 until canvasWidth.toInt()).random().toFloat(),
                    y = (0 until canvasHeight.toInt()).random().toFloat(),
                    size = 60f,
                    velocityX = randomVelocity(),
                    velocityY = randomVelocity()
                )
            )
        }
    }

    // Function to update the game state every frame
    fun updateGame() {
        // Decrement the cooldown counter
        if (fireCooldownFrames > 0) {
            fireCooldownFrames--
        }

        // Automatically shoot bullets if the cooldown is 0 and the spaceship is visible
        if (fireCooldownFrames == 0 && spaceship.isVisible) {
            shootBullet(spaceship)
            fireCooldownFrames = fireRateFrames  // Reset the cooldown
        }

        updateSpaceship()
        updateAsteroids()
        updateBullets()
        checkCollisions()

        // Check if all asteroids are destroyed and trigger the next wave
        if (asteroids.isEmpty() && !isGameOver) {
            currentWave++
            initializeAsteroids() // Spawn more asteroids in the new wave
        }
    }


    // Spaceship movement update based on angle and speed
    fun updateSpaceshipMovement(angle: Float, speedMultiplier: Float) {
        if (isRespawning) {
            return // Ignore user input while respawning
        }
        this.angle = angle
        this.speedMultiplier = speedMultiplier
    }


    // Update spaceship position based on movement and wrap around screen
    private fun updateSpaceship() {

        // If the spaceship is invincible (respawning), don't apply movement logic
        if (isRespawning) {
            return
        }

        updateSpaceshipMovement(spaceship = spaceship, angle = angle, speedMultiplier = speedMultiplier)
        // Update the ship's movement based on its velocity
        spaceship.x += spaceship.velocityX
        spaceship.y += spaceship.velocityY

        // Wrap the spaceship around the screen when it goes out of bounds
        if (spaceship.x < 0) spaceship.x = canvasWidth
        if (spaceship.x > canvasWidth) spaceship.x = 0f
        if (spaceship.y < 0) spaceship.y = canvasHeight
        if (spaceship.y > canvasHeight) spaceship.y = 0f
    }

    // Update asteroids and handle screen wrapping
    private fun updateAsteroids() {
        updateAsteroids(asteroids, canvasWidth, canvasHeight)
    }

    private fun updateBullets() {
        val bulletsToRemove = mutableListOf<Bullet>()

        bullets.forEach { bullet ->
            bullet.x += bullet.velocityX
            bullet.y += bullet.velocityY

            // Mark the bullet for removal if it goes off-screen
            if (bullet.x < 0 || bullet.x > canvasWidth || bullet.y < 0 || bullet.y > canvasHeight) {
                bulletsToRemove.add(bullet)
            }
        }

        // Remove off-screen bullets
        bullets.removeAll(bulletsToRemove)
    }

    private fun checkCollisions() {
        val asteroidsToRemove = mutableListOf<Asteroid>()
        val asteroidsToAdd = mutableListOf<Asteroid>()
        val bulletsToRemove = mutableListOf<Bullet>()  // Collect bullets for removal

        for (asteroid in asteroids) {
            if (!isRespawning && isAsteroidTouchingSpaceship(asteroid, spaceship)) {
                lives-- // Decrease lives on first collision only
                if (lives > 0) {
                    isRespawning = true
                    respawn() // Trigger respawn if lives remain
                } else {
                    isGameOver = true // Set game over state
                    return // Exit to stop further processing
                }
            }
        }

        // Check bullet-asteroid collisions
        bullets.forEach { bullet ->
            asteroids.forEach { asteroid ->
                if (isAsteroidCollidingWithBullet(asteroid, bullet)) {
                    score += 10 // Increase score when hitting an asteroid

                    bulletsToRemove.add(bullet)  // Collect the bullet for removal

                    // Mark the asteroid for removal and split it using the modular function
                    asteroidsToRemove.add(asteroid)

                    // Use the splitAsteroid function to get smaller asteroids
                    val smallerAsteroids = splitAsteroid(asteroid)

                    // Add the smaller asteroids to the list
                    asteroidsToAdd.addAll(smallerAsteroids)
                }
            }
        }

        // Remove the asteroids marked for removal
        asteroids.removeAll(asteroidsToRemove)

        // Add the smaller asteroids created from splitting
        asteroids.addAll(asteroidsToAdd)

        // Remove the bullets marked for removal
        bullets.removeAll(bulletsToRemove)
    }

    private fun respawn() {
        spaceship.isVisible = false // Hide the spaceship initially
        fireCooldownFrames = fireRateFrames * 5 // Disable shooting for the first 5 seconds

        // Start a coroutine to delay the respawn
        CoroutineScope(Dispatchers.Main).launch {
            // Pause for 5 seconds before respawn
            delay(5000L)

            // Reset the spaceship's velocity and speed multipliers to 0
            spaceship.velocityX = 0f
            spaceship.velocityY = 0f
            speedMultiplier = 0f // Reset any speed multipliers affecting movement
            angle = 0f // Reset the movement angle as well (if using direction-based movement)

            // After the delay, reset the spaceship position to a safe location
            resetSpaceshipPosition(spaceship, asteroids, canvasWidth, canvasHeight)

            // Apply invincibility for 5 more seconds after respawn
            spaceship.invincibilityTimer = System.currentTimeMillis() + 5000L

            // Start blinking during invincibility
            blinkDuringInvincibility()

            // Set respawning flag back to false after respawn is complete
            isRespawning = false
        }
    }

    private fun blinkDuringInvincibility() {
        CoroutineScope(Dispatchers.Main).launch {
            val invincibilityDuration = 5000L // 5 seconds of invincibility
            val blinkInterval = 250L // Blink every 250 milliseconds

            // Get the current time to track the invincibility period
            val startTime = System.currentTimeMillis()

            while (System.currentTimeMillis() - startTime < invincibilityDuration) {
                // Toggle the spaceship's visibility
                spaceship.isVisible = !spaceship.isVisible

                // Wait for the blink interval
                delay(blinkInterval)
            }

            // After invincibility, ensure the spaceship is visible and stop blinking
            spaceship.isVisible = true
        }
    }

    // Collision detection between an asteroid and a spaceship, respecting invincibility
    private fun isAsteroidTouchingSpaceship(asteroid: Asteroid, spaceship: Spaceship): Boolean {
        // Ignore collisions if the spaceship is invincible
        if (System.currentTimeMillis() < spaceship.invincibilityTimer) {
            return false
        }

        val distance = hypot(asteroid.x - spaceship.x, asteroid.y - spaceship.y)
        return distance < asteroid.size + spaceship.size
    }

    private fun isAsteroidCollidingWithBullet(asteroid: Asteroid, bullet: Bullet): Boolean {
        val distance = hypot(asteroid.x - bullet.x, asteroid.y - bullet.y)
        return distance < asteroid.size + bullet.size
    }

    private fun shootBullet(spaceship: Spaceship) {
        val radianAngle = Math.toRadians(spaceship.direction.toDouble())
        val bulletSpeed = 10f

        val bullet = Bullet(
            x = spaceship.x,
            y = spaceship.y,
            velocityX = (bulletSpeed * cos(radianAngle)).toFloat(),
            velocityY = (bulletSpeed * sin(radianAngle)).toFloat()
        )

        bullets.add(bullet) // Add the bullet to the bullets list
    }

    // Reset game state when game over or starting a new game
    fun resetGame() {
        // Reset spaceship position and state
        spaceship = Spaceship(
            x = canvasWidth / 2,
            y = canvasHeight / 2,
            size = 20f,
            direction = 0f
        )

        spaceship.velocityX = 0f
        spaceship.velocityY = 0f
        spaceship.isVisible = true
        spaceship.invincibilityTimer = 0L // No invincibility at the start

        // Clear game objects
        asteroids.clear()
        bullets.clear()

        // Reset game state variables
        score = 0
        lives = 3
        isGameOver = false
        isRespawning = false
        speedMultiplier = 0f
        angle = 0f
        currentWave = 1

        // Reinitialize asteroids
        initializeAsteroids()
    }


    fun setCanvas(canvasWidth: Float, canvasHeight: Float) {
        this.canvasWidth = canvasWidth
        this.canvasHeight = canvasHeight
    }

    fun resetSpaceshipPosition(spaceship: Spaceship, asteroids: List<Asteroid>, canvasWidth: Float, canvasHeight: Float) {
        var safePositionFound = false

        while (!safePositionFound) {
            val newX = (0 until canvasWidth.toInt()).random().toFloat()
            val newY = (0 until canvasHeight.toInt()).random().toFloat()

            // Check if the new position is far enough from all asteroids
            safePositionFound = asteroids.none { asteroid ->
                val distance = hypot(asteroid.x - newX, asteroid.y - newY)
                distance < asteroid.size + spaceship.size * 2 // A bit of buffer distance
            }

            // If safe position is found, reset the spaceship's position
            if (safePositionFound) {
                spaceship.x = newX
                spaceship.y = newY
            }
        }
    }

}