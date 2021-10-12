package com.github.Luc16.AStar.components

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.github.Luc16.AStar.*
import com.github.Luc16.AStar.screens.NUM_LIVES
import com.github.Luc16.AStar.utils.WallLine
import kotlin.math.sqrt

const val INVULNERABILITY_TIME = 120

class Player(var life: Int,
            private val speed: Float,
            x: Float,
            y: Float):
    Entity(x, y, Color.BLACK) {
    var invulnerabilityCounter = 0

    fun move(walls: WallLine, direction: Vector2) {
        if (life <= 0) return
        if (invulnerabilityCounter > 0) invulnerabilityCounter++
        if (invulnerabilityCounter > INVULNERABILITY_TIME) invulnerabilityCounter = 0

        var increment = speed
        if (direction.x != 0f && direction.y != 0f){
            increment = speed/ sqrt(2f)
        }
        rect.run {
            x += direction.x * increment
            if (x > WIDTH - SQ_SIZE_X) x = WIDTH - SQ_SIZE_X + 0.001f
            else if (x < 0) x = -0.001f

            walls.forEach { node ->
                if (rect.overlaps(node.rect)){
                    x = when {
                        (direction.x < 0f) -> {
                            direction.x = 0f
                            node.rect.x + node.rect.width
                        }
                        (direction.x > 0f) -> {
                            direction.x = 0f
                            node.rect.x - width
                        }
                        else -> x
                    }
                }
            }

            y += direction.y * increment
            if (y > HEIGHT - SQ_SIZE_Y) y = HEIGHT - SQ_SIZE_Y + 0.001f
            else if (y < 0) y = -0.001f

            walls.forEach { node ->
                if (rect.overlaps(node.rect)){
                    y = when {
                        (direction.y < 0f) -> {
                            direction.y = 0f
                            node.rect.y + node.rect.width
                        }
                        (direction.y > 0f) -> {
                            direction.y = 0f
                            node.rect.y - width
                        }
                        else -> y
                    }
                }
            }
        }
    }

    fun getHit(){
        if (invulnerabilityCounter == 0){
            life--
            println("VIDAS: $life")
            invulnerabilityCounter = 1
        }
    }

    override fun reset() {
        life = NUM_LIVES
        super.reset()
    }


}