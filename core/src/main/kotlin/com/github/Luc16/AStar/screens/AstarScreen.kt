package com.github.Luc16.AStar.screens

import algorithm.Position
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.github.Luc16.AStar.AStar
import com.github.Luc16.AStar.SIZE_X
import com.github.Luc16.AStar.SIZE_Y
import com.github.Luc16.AStar.components.GameGrid
import ktx.app.clearScreen
import ktx.graphics.use
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

open class AstarScreen(game: AStar, val bcColor: Color): CustomScreen(game) {
    open var grid = GameGrid(SIZE_X, SIZE_Y, bcColor)

    fun resetGrid(){
        grid = GameGrid(SIZE_X, SIZE_Y, bcColor)
    }

    open fun makeWalls() = Unit

    fun completeWall(prevPos: Position, pos: Position){
        val distCol = prevPos.col - pos.col
        val distLine = prevPos.line - pos.line
        if (distCol == 0) {
            paintLine(prevPos.line, pos.line, pos.col, true)
            return
        }
        if (distLine == 0) {
            paintLine(prevPos.col, pos.col, pos.line, false)
            return
        }

        val direction = listOf(-distLine/ abs(distLine), -distCol/ abs(distCol))
        val diagonalSize = min(abs(distCol), abs(distLine))
        paintDiagonal(direction, diagonalSize, prevPos.line, prevPos.col)
        if (abs(distLine) > abs(distCol)){
            paintLine(prevPos.line + diagonalSize*direction[0], pos.line, pos.col, true)
        }
        else
            paintLine(prevPos.col + diagonalSize*direction[1], pos.col, pos.line, false)
    }

    private fun paintLine(begin: Int, end: Int, other:Int, isLine: Boolean){
        val b = min(begin, end)
        val e = max(begin, end)
        for (i in (b + 1)..e){
            val pos = if (isLine) Position(i, other) else Position(other, i)
            grid.getNodeInGrid(pos).run {
                isTraversable = false
                color = Color.BROWN
            }
        }
    }
    private fun paintDiagonal(direction: List<Int>, diagonalSize: Int, line: Int, col: Int){
        for (i in 1 .. diagonalSize){
            val p = Position(line + i*direction[0], col + i*direction[1])
            grid.getNodeInGrid(p).run {
                isTraversable = false
                color = Color.BROWN
            }
        }
    }

     fun draw(){
        clearScreen(0f, 0f, 0f, 0f)
        renderer.use(ShapeRenderer.ShapeType.Filled) {
            grid.draw(renderer)
        }
    }

}