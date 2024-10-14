package com.example.minigames.games.systems

class CollisionDetector {

    // Check collision between a circle (bird) and a rectangle (pipe)
    fun isCircleCollidingWithRectangle(circleX: Float, circleY: Float, radius: Float, rectX: Float, rectY: Float, rectWidth: Float, rectHeight: Float): Boolean {
        // Find the closest point on the rectangle to the center of the circle
        val closestX = circleX.coerceIn(rectX, rectX + rectWidth)
        val closestY = circleY.coerceIn(rectY, rectY + rectHeight)

        // Calculate the distance between the circle's center and the closest point on the rectangle
        val distanceX = circleX - closestX
        val distanceY = circleY - closestY

        // If the distance is less than the circle's radius, there is a collision
        val distanceSquared = (distanceX * distanceX) + (distanceY * distanceY)
        return distanceSquared < (radius * radius)
    }
}