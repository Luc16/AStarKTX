package com.github.Luc16.AStar.screens

import algorithm.Position
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.github.Luc16.AStar.*
import com.github.Luc16.AStar.components.Enemy
import com.github.Luc16.AStar.components.GameGrid
import com.github.Luc16.AStar.components.Node
import com.github.Luc16.AStar.components.Player
import com.github.Luc16.AStar.utils.WallLine
import ktx.graphics.use
import kotlin.concurrent.thread

const val MAX_NUM_WALLS = 100
const val NUM_LIVES = 3

class GameScreen(game: AStar, bcColor: Color): AstarScreen(game, bcColor) {
    private var prevDraw: Position? = null
    private val wallLine: WallLine = WallLine(MAX_NUM_WALLS)
    private val enemies = listOf(Enemy(4f, 10f, 10f), Enemy(4f, 1000f, 600f))
    private val player = Player(NUM_LIVES, 4f, 200f, 200f)

    override fun render(delta: Float) {
        if (player.life <= 0) endGame()

        updateEnemies()
        updatePlayer()
        makeWalls()

        if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) game.setScreen<AlgorithmScreen>()

        draw { renderer ->
            player.draw(renderer)
            enemies.forEach { enemy -> enemy.draw(renderer) }
        }
        batch.use {
            font.draw(batch, "Vidas: ${player.life}", WIDTH - 200f, HEIGHT - 30f)
        }
    }

    private fun endGame(){
        player.reset()
        enemies.forEach { enemy -> enemy.reset() }
        game.setScreen<MenuScreen>()
        resetGrid()
        prevDraw = null
        wallLine.removeAll()
    }

    private fun updateEnemies(){
        var allStuck = true
        enemies.forEach { enemy ->
            enemy.move(grid, player.worldToGrid(grid).pos, enemies)
            if (!enemy.noMovementAvailable()) allStuck = false
            if (enemy.rect.overlaps(player.rect)) player.getHit()
        }
        if (allStuck) wallLine.removeAll()
    }

    private fun updatePlayer(){
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
