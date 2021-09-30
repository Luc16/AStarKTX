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
import ktx.graphics.use

const val SIZE_X = 128
const val SIZE_Y = 72
val BACKGROUD_COLOR = Color.LIGHT_GRAY

class AlgorithmScreen(game: AStar): CustomScreen(game) {
    private var grid = GameGrid(SIZE_X, SIZE_Y, BACKGROUD_COLOR)
    private var start = Position(0, 0)
    private var end = Position(0, 0)
    private var open: Heap<Node> = Heap<Node>(SIZE_X*SIZE_Y)
    private var startAnimation = false

    override fun render(delta: Float) {
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) game.setScreen<Screen1>()
        if (!startAnimation) runAlgo()
        else runAnimation()
        draw()

    }
    
    private fun runAnimation(){
        open = grid.animatedAstar(start, end, open)
        if (open.isEmpty()) startAnimation = false
    }
    
    private fun runAlgo(){
        makeWalls()
        when{
            Gdx.input.isKeyPressed(Input.Keys.R) -> grid = GameGrid(SIZE_X, SIZE_Y, BACKGROUD_COLOR)
            Gdx.input.isKeyPressed(Input.Keys.P) -> grid.resetPath()
            Gdx.input.isKeyJustPressed(Input.Keys.ENTER) ->{
                grid.resetPath()
                val startTime = TimeUtils.millis()
                grid.shortestPath(start, end)
                println("O tempo que o algoritimo demorou foi: ${TimeUtils.timeSinceMillis(startTime)} milisegundos")
            }
            Gdx.input.isKeyJustPressed(Input.Keys.SPACE) -> {
                grid.resetPath()
                open.add(grid.getNodeInGrid(start))
                startAnimation = true
            }
        }
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
                            color = defaultColor
                        }
                        rect.contains(mouse) && Gdx.input.isKeyPressed(Input.Keys.S) -> {
                            grid.getNodeInGrid(start).run {
                                color = defaultColor
                                isTraversable = true
                            }
                            start = pos
                            color = Color.BLACK
                        }
                        rect.contains(mouse) && Gdx.input.isKeyJustPressed(Input.Keys.E) -> {
                            if (isTraversable){
                                grid.getNodeInGrid(end).run {
                                    color = defaultColor
                                    isTraversable = true
                                }
                                end = pos
                                color = Color.WHITE
                            }
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
}
