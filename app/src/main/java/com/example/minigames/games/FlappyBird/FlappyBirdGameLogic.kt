package com.example.minigames.games.FlappyBird

import com.example.minigames.games.systems.CollisionDetector

class FlappyBirdGameLogic(private var canvasWidth: Float = 1440f, private var canvasHeight: Float = 1526f) {


    // Initialize CollisionDetector
    private val collisionDetector = CollisionDetector()

    // Create a bird instance
    lateinit var bird: Bird

    // Pipe attributes
    val pipes = mutableListOf<Pipe>()
    var pipeWidth = 0f
    private var pipeSpacing = 0f
    private var pipeSpeed = 0f

    // Game state
    var score: Int = 0
    var isGameOver: Boolean = false
    var deathCount: Int = 0 // Track how many times the bird has died

    // Initialize the game with canvas size
    fun startNewGame() {
        // Initialize the bird with canvas-scaled size
        bird = Bird(
            x = canvasWidth * 0.1f, // Keep a bit to the left of the middle horizontally for movement space
            y = canvasHeight / 2,   // Middle of the screen vertically
            width = canvasWidth * 0.05f,
            height = canvasHeight * 0.05f
        )

        // Initialize other game elements (pipes, etc.)
        pipeWidth = canvasWidth * 0.1f
        pipeSpacing = canvasWidth * 0.5f // Increase spacing between pipes for more distance between them
        pipeSpeed = canvasWidth * 0.009f // Increase pipe speed to make the game harder/faster
        pipes.clear()
        isGameOver = false

        // Generate initial pipes - all start off-screen
        for (i in 0..2) {
            // Each pipe is placed one full screen width off to the right
            generateNewPipe(canvasWidth + i * pipeSpacing) // Starting off-screen
        }
    }


    // Bird flaps (user input)
    fun birdFlap() {
        if (!isGameOver) {
            bird.flap()
        }
    }

    fun updateGame() {
        // Update bird's position (apply gravity)
        bird.updatePosition()

        // Prevent bird from going off the top of the screen
        if (bird.y < 0f) {
            bird.y = 0f
            bird.velocity = 0f // Reset velocity if bird is at the top
        }

        // Check if the bird hits the ground (game over)
        if (bird.y + bird.height > canvasHeight) {
            isGameOver = true
            triggerGameOver()
        }

        // Move pipes and check if the bird passed one
        for (pipe in pipes) {
            pipe.x -= pipeSpeed // Move the pipe to the left

            // If the bird passes a pipe, increase the score
            if (!pipe.passed && pipe.x + pipeWidth < bird.x) {
                score++
                pipe.passed = true
            }

            // Recycle pipes when they move off-screen
            if (pipe.x + pipeWidth < 0) {
                recyclePipe(pipe)
            }
        }

        // Handle bird-pipe collision detection
        checkCollision()
    }

    private fun generateNewPipe(xPosition: Float) {
        val gapHeight = canvasHeight * 0.2f // Gap size between top and bottom pipes
        val pipeHeight = (Math.random() * (canvasHeight * 0.5f) + (canvasHeight * 0.1f)).toFloat() // Random pipe height
        pipes.add(Pipe(xPosition, pipeHeight, gapHeight))
    }

    // Recycle a pipe by moving it to the far right with a new random height
    private fun recyclePipe(pipe: Pipe) {
        pipe.x = pipes.maxByOrNull { it.x }?.x?.plus(pipeSpacing) ?: canvasWidth
        pipe.height = (Math.random() * (canvasHeight * 0.5f) + (canvasHeight * 0.1f)).toFloat()
        pipe.passed = false
    }

    // Check for collision (bird vs. pipes or bird vs. ground)
    private fun checkCollision() {
        // Check if the bird hits the ground
        if (bird.y + bird.height > canvasHeight) {
            isGameOver = true
            return // Game over if bird hits the ground
        }

        // Check if bird collides with any of the pipes
        for (pipe in pipes) {
            // Check for collision with the top pipe
            if (collisionDetector.isCircleCollidingWithRectangle(
                    circleX = bird.x + bird.width / 2, // Circle's X position (center of bird)
                    circleY = bird.y + bird.height / 2, // Circle's Y position (center of bird)
                    radius = bird.width / 2,            // Circle's radius (bird's width / 2)
                    rectX = pipe.x,                     // Rectangle's X position (top or bottom pipe)
                    rectY = 0f,                         // Top of the screen (top pipe)
                    rectWidth = pipeWidth,              // Pipe width
                    rectHeight = pipe.height            // Top pipe height
                )
            ) {
                triggerGameOver()
                return // End the game if there's a collision with the top pipe
            }

            // Check for collision with the bottom pipe
            if (collisionDetector.isCircleCollidingWithRectangle(
                    circleX = bird.x + bird.width / 2, // Circle's X position (center of bird)
                    circleY = bird.y + bird.height / 2, // Circle's Y position (center of bird)
                    radius = bird.width / 2,            // Circle's radius (bird's width / 2)
                    rectX = pipe.x,                     // Rectangle's X position (bottom pipe)
                    rectY = pipe.height + pipe.gap,     // Bottom pipe's Y start (below the gap)
                    rectWidth = pipeWidth,              // Pipe width
                    rectHeight = canvasHeight - (pipe.height + pipe.gap) // Height of the bottom pipe
                )
            ) {
                triggerGameOver()
                return // End the game if there's a collision with the bottom pipe
            }
        }
    }

    // Trigger game over and increment death counter
    private fun triggerGameOver() {
        isGameOver = true
        deathCount++ // Increment the death counter each time the game ends
    }

    // Reset the game logic (bird position, pipe position, score, etc.)
    fun resetGame() {
        score = 0
        isGameOver = false
        bird.y = canvasHeight / 2 // Reset the bird's position
        bird.velocity = 0f // Reset bird's velocity

        // Reset pipes
        pipes.clear()
        for (i in 0..2) {
            generateNewPipe(i * pipeSpacing + canvasWidth)
        }
    }

    fun setCanvas(canvasWidth: Float, canvasHeight: Float) {
        this.canvasWidth = canvasWidth
        this.canvasHeight = canvasHeight
    }
}