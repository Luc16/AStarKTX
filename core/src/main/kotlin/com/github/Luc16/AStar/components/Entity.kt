package com.github.Luc16.AStar.components

import algorithm.Position
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.github.Luc16.AStar.*
import java.lang.IndexOutOfBoundsException

open class Entity(
    private val initialX: Float,
    private val initialY: Float) {
    val rect = Rectangle(initialX, initialY, WIDTH /SIZE_X.toFloat(), HEIGHT /SIZE_Y.toFloat())
    val sprite = Sprite()

    fun worldToGrid(grid: GameGrid): Node {
        var pos = Position(((HEIGHT - rect.y - rect.width/2)/(HEIGHT/SIZE_Y)).toInt(),
            ((rect.x + rect.width/2)/(WIDTH/SIZE_X)).toInt())
        if (pos.line >= grid.grid.size) pos = Position(grid.grid.size-1, pos.col)
        if (pos.col >= grid.grid[0].size) pos = Position(pos.line, grid.grid[0].size-1)
        return grid.getNodeInGrid(pos)
    }

    fun setSpriteRegion(region: Texture, flip: Boolean = false){
        sprite.run {
            setRegion(region)
            setSize(SQ_SIZE_X, SQ_SIZE_Y)
            setFlip(flip, false)
        }
    }

    fun draw(batch: Batch){
        sprite.setPosition(rect.x, rect.y)
        sprite.draw(batch)
    }

    open fun reset(){
        rect.x = initialX
        rect.y = initialY
    }
}