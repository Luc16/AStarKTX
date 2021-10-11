package com.github.Luc16.AStar.screens

import algorithm.Position
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.github.Luc16.AStar.AStar
import com.github.Luc16.AStar.HEIGHT
import com.github.Luc16.AStar.SIZE_X
import com.github.Luc16.AStar.SIZE_Y
import com.github.Luc16.AStar.components.Enemy
import com.github.Luc16.AStar.utils.WallLine

const val MAX_NUM_WALLS = 100

class GameScreen(game: AStar, bcColor: Color): AstarScreen(game, bcColor) {
    private var prevDraw: Position? = null
    private val wallLine: WallLine = WallLine(MAX_NUM_WALLS)
    private val e = Enemy(1, 4f, 10f, 10f)

    override fun render(delta: Float) {
        e.move(grid)
        when {
            Gdx.input.isKeyPressed(Input.Keys.R) -> {
                resetGrid()
                wallLine.removeAll()
                prevDraw = null
            }
            Gdx.input.isKeyPressed(Input.Keys.NUM_2) -> game.setScreen<AlgorithmScreen>()
        }
        makeWalls()
        draw(e, resetNodes = true)
    }

    override fun makeWalls(){
        val mouse = Vector2(Gdx.input.x.toFloat(), HEIGHT - Gdx.input.y.toFloat())
        if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) prevDraw = null
        grid.grid.forEach { line ->
            line.forEach { node ->
                node.run {
                    when {
                        rect.contains(mouse) && Gdx.input.isButtonPressed(Input.Buttons.LEFT) -> {
                            if (isTraversable){
                                this.becomeWall()
                                prevDraw?.let { prevPos -> fillWalls(prevPos, pos){ wallLine.add(it) } }
                                wallLine.add(this)
                                prevDraw = pos
                            }
                        }
                        rect.contains(mouse) && Gdx.input.isButtonPressed(Input.Buttons.RIGHT) -> {
                            isTraversable = true
                            color = Color.NAVY
                            wallLine.updateLine()
                        }
                        rect.contains(mouse) && Gdx.input.isKeyJustPressed(Input.Keys.M) -> e.calculatePath(grid, pos)

                    }
                }
            }
        }
    }
}
