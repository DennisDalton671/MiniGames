package com.example.minigames.games.FlappyBird

data class Pipe(
    var x: Float,       // X position of the pipe
    var height: Float,  // Height of the pipe
    val gap: Float,     // Gap between top and bottom pipe
    var passed: Boolean = false // Whether the bird has passed this pipe
)