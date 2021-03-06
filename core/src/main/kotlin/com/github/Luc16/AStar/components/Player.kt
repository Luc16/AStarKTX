package com.github.Luc16.AStar.components

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
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
    Entity(x, y) {
    var invulnerabilityCounter = 0
    private val textures = mapOf(
        "up" to Texture(Gdx.files.internal("assets/up.jpeg")),
        "down" to Texture(Gdx.files.internal("assets/down.jpeg")),
        "ur" to Texture(Gdx.files.internal("assets/ur.jpeg")),
        "dl" to Texture(Gdx.files.internal("assets/dl.jpeg")),
        "right" to Texture(Gdx.files.internal("assets/right.jpeg")),
        "idle" to Texture(Gdx.files.internal("assets/idle.jpeg"))
    )

    init {
        textures["idle"]?.let { setSpriteRegion(it) }
    }

    fun move(walls: WallLine, direction: Vector2): Vector2 {
        if (life <= 0) return Vector2(0f, 0f)
        val moveAmount = Vector2(rect.x, rect.y)

        if (invulnerabilityCounter > 0) invulnerabilityCounter++
        if (invulnerabilityCounter > INVULNERABILITY_TIME) {
            sprite.setAlpha(1f)
            invulnerabilityCounter = 0
        }

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
        moveAmount.x = rect.x - moveAmount.x
        moveAmount.y = rect.y - moveAmount.y
        return moveAmount
    }

    fun getHit(){
        if (invulnerabilityCounter == 0){
            sprite.setAlpha(0.5f)
            life--
            invulnerabilityCounter = 1
        }
    }

    fun updateSpriteDirection(direction: Vector2) {
        when {
            direction.epsilonEquals(Vector2(0f, 1f)) -> textures["up"]?.let { setSpriteRegion(it) }
            direction.epsilonEquals(Vector2(0f, -1f)) -> textures["down"]?.let { setSpriteRegion(it) }
            direction.epsilonEquals(Vector2(1f, 1f)) -> textures["ur"]?.let { setSpriteRegion(it) }
            direction.epsilonEquals(Vector2(-1f, 1f)) -> textures["ur"]?.let { setSpriteRegion(it, true) }
            direction.epsilonEquals(Vector2(-1f, -1f)) -> textures["dl"]?.let { setSpriteRegion(it) }
            direction.epsilonEquals(Vector2(1f, -1f)) -> textures["dl"]?.let { setSpriteRegion(it, true) }
            direction.epsilonEquals(Vector2(1f, 0f)) -> textures["right"]?.let { setSpriteRegion(it) }
            direction.epsilonEquals(Vector2(-1f, 0f)) -> textures["right"]?.let { setSpriteRegion(it, true) }
            else -> textures["idle"]?.let { setSpriteRegion(it) }
        }
    }

    override fun reset() {
        life = NUM_LIVES
        invulnerabilityCounter = 0
        sprite.setAlpha(1f)
        super.reset()
    }
}