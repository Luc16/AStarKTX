package com.github.Luc16.AStar.components

import algorithm.Position
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.github.Luc16.AStar.HEIGHT
import com.github.Luc16.AStar.SIZE_X
import com.github.Luc16.AStar.SIZE_Y
import com.github.Luc16.AStar.WIDTH

open class Entity(
    private val initialX: Float,
    private val initialY: Float,
    private val color: Color) {
    val rect = Rectangle(initialX, initialY, WIDTH /SIZE_X.toFloat(), HEIGHT /SIZE_Y.toFloat())

    fun worldToGrid(grid: GameGrid): Node{
        val pos = Position(((HEIGHT - rect.y - rect.width/2)/(HEIGHT/SIZE_Y)).toInt(),
            ((rect.x + rect.width/2)/(WIDTH/SIZE_X)).toInt())
        return grid.getNodeInGrid(pos)
    }

    fun draw(renderer: ShapeRenderer){
        renderer.color = color
        renderer.rect(rect.x, rect.y, rect.width, rect.height)
    }

    open fun reset(){
        rect.x = initialX
        rect.y = initialY
    }
}