package com.github.Luc16.AStar.screens

import algorithm.Position
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.github.Luc16.AStar.AStar
import com.github.Luc16.AStar.SIZE_X
import com.github.Luc16.AStar.SIZE_Y
import com.github.Luc16.AStar.components.Enemy
import com.github.Luc16.AStar.components.Entity
import com.github.Luc16.AStar.components.GameGrid
import com.github.Luc16.AStar.components.Node
import ktx.app.clearScreen
import ktx.graphics.use
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

open class AstarScreen(game: AStar, private val bcColor: Color): CustomScreen(game) {
    open var grid = GameGrid(SIZE_X + 1, SIZE_Y + 1, bcColor)

    fun resetGrid(){
        grid = GameGrid(SIZE_X + 1, SIZE_Y + 1, bcColor)
    }

    open fun makeWalls() = Unit

    fun fillWalls(prevPos: Position, pos: Position, func: (node:Node) -> Unit = {}){
        val distCol = prevPos.col - pos.col
        val distLine = prevPos.line - pos.line

        val dir = listOf(if (distLine < 0) - 1 else 1, if (distCol < 0) - 1 else 1)
        if (abs(distLine) > abs(distCol)){
            paintLine(prevPos, distLine, distCol, dir, 0, func)
        } else {
            paintLine(prevPos, distCol, distLine, dir, 1, func)
        }
    }

    private fun paintLine(prevPos: Position, maxDist: Int, minDist: Int, dir:List<Int>, dirIdx: Int, func: (node:Node) -> Unit = {}){
        for (i in 1..abs(maxDist)){
            val m: Float = if (minDist == 0) 0f else (minDist.toFloat()/maxDist)
            val newPos = Position(prevPos.line - (m * i).toInt()*dirIdx*dir[dirIdx] - i*dir[dirIdx]*(1-dirIdx),
                prevPos.col - i*dir[dirIdx]*dirIdx - (m * i).toInt()*(1 - dirIdx)*dir[dirIdx])
            grid.getNodeInGrid(newPos).run {
                if (this.isTraversable){
                    func(this)
                }
            }
        }
    }

    open fun draw(render: (Batch) -> Unit){
        clearScreen(0f, 0f, 0f, 0f)
        renderer.use(ShapeRenderer.ShapeType.Filled) {
            grid.draw(renderer)
        }
        batch.use {
            render(batch)
        }
    }

}
