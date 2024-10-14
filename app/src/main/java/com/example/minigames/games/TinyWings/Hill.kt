package com.example.minigames.games.TinyWings

data class Hill(
    val startY: Float,    // The starting height of the hill
    val midY: Float,      // The peak (or valley) height of the hill
    val endY: Float,      // The ending height of the hill (connects to the next hill)
    val isHill: Boolean   // True if it's a hill (convex), false if it's a dip (concave)
)