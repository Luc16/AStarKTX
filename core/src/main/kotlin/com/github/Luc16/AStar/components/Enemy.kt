package com.github.Luc16.AStar.components

import algorithm.Position
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.github.Luc16.AStar.HEIGHT
import com.github.Luc16.AStar.SQ_SIZE_X
import com.github.Luc16.AStar.SQ_SIZE_Y
import com.github.Luc16.AStar.WIDTH
import kotlin.math.sqrt
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

const val MIN_MOVES = 30

class Enemy(
    private val initialSpeed: Float,
    x: Float,
    y: Float,
    var grid: GameGrid
) :
    Entity(x, y) {

    private var counter = 0
    private var directions = listOf<Vector2>()
    private val startPos = Vector2()
    private var moveCounter = 0
    var speed = initialSpeed
    private val enemyNode get() = worldToGrid(grid)

    init {
        setSpriteRegion(Texture(Gdx.files.internal("assets/enemy.jpeg")))
    }

    private fun calculatePath(target: Position) {
        rect.x = enemyNode.rect.x
        rect.y = enemyNode.rect.y
        startPos.set(rect.x, rect.y)
        directions = grid.shortestPath(enemyNode.pos, target, false)
        counter = 0
        grid.resetPath()
    }

    private fun generateNewPath(target: Position) {
        moveCounter = 0
        calculatePath(target)
    }

    fun noMovementAvailable(): Boolean = directions.isNotEmpty() && (directions[0].x == 0f && directions[0].y == 0f)

    fun move(target: Position, enemies: List<Enemy>) {
        if (counter >= directions.size || noMovementAvailable() || directions.isEmpty()) {
            generateNewPath(target)
            return
        }
        rect.run {
            var increment = speed
            if (directions[counter].x != 0f && directions[counter].y != 0f) {
                increment = speed / sqrt(2f)
            }
            x += directions[counter].x * increment
            y += directions[counter].y * increment

            if (y > HEIGHT - SQ_SIZE_Y) y = HEIGHT - SQ_SIZE_Y - 0.01f
            else if (y < 0) y = 0.01f
            if (x > WIDTH - SQ_SIZE_X) {
                x = WIDTH - SQ_SIZE_X - 0.01f
                if (collidingWithOther(enemies)){
                    y -= SQ_SIZE_Y
                }
            }
            else if (x < 0) x = 0.01f

            if (!enemyNode.isTraversable || collidingWithOther(enemies)) {
                x -= 2 * directions[counter].x * increment
                y -= 2 * directions[counter].y * increment
                enemyNode.run {
                    if (!isTraversable) {
                        becomeTraversable()
                    }
                    rect.x = rect.x
                    rect.y = rect.y
                }
                directions = listOf()
                counter = 0
                return
            }

            if (x >= (startPos.x + SQ_SIZE_X) || x <= (startPos.x - SQ_SIZE_X) ||
                y >= (startPos.y + SQ_SIZE_Y) || y <= (startPos.y - SQ_SIZE_Y)
            ) {
                x = startPos.x + SQ_SIZE_X * directions[counter].x
                y = startPos.y + SQ_SIZE_Y * directions[counter].y
                startPos.set(rect.x, rect.y)
                counter++
                if (moveCounter > MIN_MOVES) generateNewPath(target)
            }
        }
        moveCounter++
    }

    private fun collidingWithOther(enemies: List<Enemy>): Boolean {
        enemies.forEach { enemy ->
            if (enemy == this) return@forEach
            if (enemy.rect.overlaps(rect)){
                return true
            }
        }
        return false
    }

    override fun reset() {
        counter = 0
        directions = listOf()
        startPos.set(0f, 0f)
        moveCounter = 0
        speed = initialSpeed
        super.reset()
    }

}