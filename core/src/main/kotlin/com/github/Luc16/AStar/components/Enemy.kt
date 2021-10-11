package com.github.Luc16.AStar.components

import algorithm.Position
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.github.Luc16.AStar.SIZE_X
import com.github.Luc16.AStar.SIZE_Y
import com.github.Luc16.AStar.SQ_SIZE_X
import com.github.Luc16.AStar.SQ_SIZE_Y
import kotlin.math.sqrt

class Enemy(private val life: Int,
            private val speed: Float,
            x: Float,
            y: Float):
    Entity(x, y, Color.GOLD) {

    private var counter = 0
    private var directions = listOf<Vector2>()
    private val startPos = Vector2()

    fun calculatePath(grid: GameGrid, target: Position){
        val node = worldToGrid(grid)
        rect.x = node.rect.x
        rect.y = node.rect.y
        startPos.set(rect.x, rect.y)
        directions = grid.shortestPath(node.pos, target, false)
        counter = 0
    }

    fun move(grid: GameGrid) {
        if (counter >= directions.size) return
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
                worldToGrid(grid).rect.run {
                    rect.x = x
                    rect.y = y
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
            }

        }
    }


}