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
import com.github.Luc16.AStar.components.GameGrid
import com.github.Luc16.AStar.components.Node
import com.github.Luc16.AStar.components.Player
import com.github.Luc16.AStar.utils.WallLine
import kotlin.concurrent.thread

const val MAX_NUM_WALLS = 100
const val NUM_LIVES = 3

class GameScreen(game: AStar, bcColor: Color): AstarScreen(game, bcColor) {
    private var prevDraw: Position? = null
    private val wallLine: WallLine = WallLine(MAX_NUM_WALLS)
    private val enemies = listOf(Enemy(4f, 10f, 10f), Enemy(4f, 1000f, 600f))
    private val player = Player(NUM_LIVES, 4f, 200f, 200f)

    override fun render(delta: Float) {
        if (player.life > 0){
            enemies.forEach { enemy ->
                enemy.move(grid, player.worldToGrid(grid).pos)
                if (enemy.rect.overlaps(player.rect)) player.getHit()
            }
            playerMovement()
            makeWalls()
        } else endGame()

        if (Gdx.input.isKeyPressed(Input.Keys.NUM_2))  game.setScreen<AlgorithmScreen>()
        draw(player, enemies = enemies)
    }

    private fun endGame(){
        player.reset()
        enemies.forEach { enemy -> enemy.reset() }
        game.setScreen<MenuScreen>()
        resetGrid()
        prevDraw = null
        wallLine.removeAll()
    }

    private fun playerMovement(){
        val direction = Vector2()
        if(Gdx.input.isKeyPressed(Input.Keys.W)) direction.y = 1f
        if(Gdx.input.isKeyPressed(Input.Keys.A)) direction.x = -1f
        if(Gdx.input.isKeyPressed(Input.Keys.S)) direction.y = -1f
        if(Gdx.input.isKeyPressed(Input.Keys.D)) direction.x = 1f
        player.move(wallLine, direction)
    }

    private fun Node.isNextToPlayer() : Boolean {
        var nextToPlayer = false
        val playerNode = player.worldToGrid(grid)
        if (this == playerNode) return true

        this.forEachNeighbor(grid){ neighbor, i, j ->
            if (neighbor == playerNode){
                nextToPlayer = true
                return@forEachNeighbor
            }
        }
        return nextToPlayer
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
                                prevDraw?.let { prevPos -> fillWalls(prevPos, pos) { node ->
                                    if (!node.isNextToPlayer()) {
                                        wallLine.add(node)
                                        becomeWall()
                                    }

                                } }
                                if (node.isNextToPlayer()) becomeTraversable()
                                else wallLine.add(this)
                                prevDraw = pos
                            }
                        }
                        rect.contains(mouse) && Gdx.input.isButtonPressed(Input.Buttons.RIGHT) -> {
                            isTraversable = true
                            color = Color.NAVY
                            wallLine.updateLine()
                        }
                    }
                }
            }
        }
    }
}
