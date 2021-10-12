package com.github.Luc16.AStar.components

import algorithm.Position
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.github.Luc16.AStar.SQ_SIZE_X
import com.github.Luc16.AStar.SQ_SIZE_Y
import kotlin.math.sqrt
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

const val MIN_MOVES = 40

class Enemy(private val speed: Float,
            x: Float,
            y: Float):
    Entity(x, y, Color.GOLD) {

    private var counter = 0
    private var directions = listOf<Vector2>()
    private val startPos = Vector2()
    private var moveCounter = 0

    private fun calculatePath(grid: GameGrid, target: Position){
        val node = worldToGrid(grid)
        rect.x = node.rect.x
        rect.y = node.rect.y
        startPos.set(rect.x, rect.y)
        directions = grid.shortestPath(node.pos, target, false)
        counter = 0
        grid.resetPath()
    }

    private fun generateNewPath(grid: GameGrid, target: Position){
        moveCounter = 0
        runBlocking {
            launch {
                calculatePath(grid, target)
            }
        }
    }

    private fun List<Vector2>.noMovementAvailable(): Boolean {
        return this.isEmpty() || (this[0].x == 0f && this[0].y == 0f)
    }

    fun move(grid: GameGrid, target: Position) {
        if (counter >= directions.size || directions.noMovementAvailable()) {
            generateNewPath(grid, target)
            return
        }
        rect.run {
            var increment = speed
            if (directions[counter].x != 0f && directions[counter].y != 0f){
                increment = speed/ sqrt(2f)
            }
            x += directions[counter].x * increment
            y += directions[counter].y * increment

            if (!worldToGrid(grid).isTraversable){
                x -= 2*directions[counter].x * increment
                y -= 2*directions[counter].y * increment
                worldToGrid(grid).run {
                    if (!isTraversable){
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
                y >= (startPos.y + SQ_SIZE_Y) ||  y <= (startPos.y - SQ_SIZE_Y)){
                x = startPos.x + SQ_SIZE_X*directions[counter].x
                y = startPos.y + SQ_SIZE_Y*directions[counter].y
                startPos.set(rect.x, rect.y)
                counter++
                if (moveCounter > MIN_MOVES) generateNewPath(grid, target)
            }
        }
        moveCounter++
    }

    override fun reset() {
        counter = 0
        directions = listOf()
        startPos.set(0f, 0f)
        moveCounter = 0
        super.reset()
    }

}