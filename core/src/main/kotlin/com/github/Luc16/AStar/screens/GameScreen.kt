package com.github.Luc16.AStar.screens

import algorithm.Heap
import algorithm.Position
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.TimeUtils
import com.github.Luc16.AStar.AStar
import com.github.Luc16.AStar.HEIGHT
import com.github.Luc16.AStar.SIZE_X
import com.github.Luc16.AStar.SIZE_Y
import com.github.Luc16.AStar.components.GameGrid
import com.github.Luc16.AStar.components.Node
import ktx.app.clearScreen
import ktx.graphics.circle
import ktx.graphics.use
import kotlin.concurrent.thread
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class GameScreen(game: AStar, bcColor: Color): AstarScreen(game, bcColor) {
    private var prevDraw: Position? = null

    override fun render(delta: Float) {
        when {
            Gdx.input.isKeyPressed(Input.Keys.R) -> {
                resetGrid()
                prevDraw = null
            }
            Gdx.input.isKeyPressed(Input.Keys.NUM_2) -> game.setScreen<AlgorithmScreen>()
        }
        makeWalls()
        draw()
    }

    override fun makeWalls(){
        val mouse = Vector2(Gdx.input.x.toFloat(), HEIGHT - Gdx.input.y.toFloat())
        if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) prevDraw = null
        grid.grid.forEach { line ->
            line.forEach { node ->
                node.run {
                    when {
                        rect.contains(mouse) && Gdx.input.isButtonPressed(Input.Buttons.LEFT) -> {
                            isTraversable = false
                            color = Color.BROWN
                            prevDraw?.let { prevPos -> completeWall(prevPos, pos)}
                            prevDraw = pos
                        }
                        rect.contains(mouse) && Gdx.input.isButtonPressed(Input.Buttons.RIGHT) -> {
                            isTraversable = true
                            color = Color.NAVY
                        }
                    }
                }
            }
        }
    }
}
