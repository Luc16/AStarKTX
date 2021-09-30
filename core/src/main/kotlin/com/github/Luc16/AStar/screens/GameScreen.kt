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
import com.github.Luc16.AStar.components.GameGrid
import com.github.Luc16.AStar.components.Node
import ktx.app.clearScreen
import ktx.graphics.circle
import ktx.graphics.use
import kotlin.concurrent.thread

class GameScreen(game: AStar): CustomScreen(game) {
    private var grid = GameGrid(SIZE_X, SIZE_Y, Color.NAVY)
    private var running = true

    override fun show() {
        thread(start = true){
            var counter = 0
            while (running){
                counter++
                println("${Thread.currentThread()} has run $counter times.")
            }
        }
    }

    override fun render(delta: Float) {
//        println("The FPS is: ${1/delta}")
        makeWalls()
        draw()
    }

    private fun makeWalls(){
        val mouse = Vector2(Gdx.input.x.toFloat(), HEIGHT - Gdx.input.y.toFloat())
        grid.grid.forEach { line ->
            line.forEach { node ->
                node.run {
                    when {
                        rect.contains(mouse) && Gdx.input.isButtonPressed(Input.Buttons.LEFT) -> {
                            isTraversable = false
                            color = Color.BROWN
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

    private fun draw(){
        clearScreen(0f, 0f, 0f, 0f)
        renderer.use(ShapeRenderer.ShapeType.Filled) {
            grid.draw(renderer)
        }
    }

    override fun dispose() {
        running = false
        super.dispose()
    }
}
