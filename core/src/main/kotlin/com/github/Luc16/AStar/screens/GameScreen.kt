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
import kotlin.math.roundToInt

const val MAX_NUM_WALLS = 100
const val NUM_LIVES = 3
const val NUM_ENEMIES = 6
const val SPEED_INCREASE = 0.3f

class GameScreen(game: AStar, bcColor: Color): AstarScreen(game, bcColor) {
    private var prevDraw: Position? = null
    private val wallLine: WallLine = WallLine(MAX_NUM_WALLS)
    private val enemies = List(NUM_ENEMIES) { i ->
        val y = if (i % 2 == 0) HEIGHT - 50f else 50f
        Enemy(3f, (i % NUM_ENEMIES/2) * (WIDTH - 100f) / (NUM_ENEMIES / 2) + 50f, y, grid)
    }
    private val player = Player(NUM_LIVES, 3f, WIDTH/2f, HEIGHT/2f)
    private var score = 0
    private val file = Gdx.files.local("assets/score.txt")

    override fun render(delta: Float) {
        if (player.life <= 0) endGame()

        updateEnemies()
        updatePlayer()
        makeWalls()

        if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) game.setScreen<AlgorithmScreen>()

        draw { batch ->
            player.draw(batch)
            enemies.forEach { enemy -> enemy.draw(batch) }
            font.draw(batch, "Lives: ${player.life}", WIDTH - 200f, HEIGHT - 30f)
            font.draw(batch, "Score: ${(1000*score/120f).toInt()}", WIDTH/2f - 100f, HEIGHT - 30f)
        }
        score++
    }

    private fun endGame(){
        player.reset()
        resetGrid()
        enemies.forEach { enemy ->
            enemy.reset()
            enemy.grid = grid
        }
        prevDraw = null
        wallLine.removeAll()
        val scores = file.readString().split(" ")
        file.writeString(
            (if ((1000*score/120f).toInt() > scores[0].toInt()) "${(1000*score/120f).toInt()}" else scores[0]) +
                    " ${(1000*score/120f).toInt()}",
            false
        )
        score = 0
        game.setScreen<MenuScreen>()
    }

    private fun updateEnemies(){
        var numStuck = 0
        enemies.forEach { enemy ->
            if (score % 1200 == 0) enemy.speed += SPEED_INCREASE
            if (player.invulnerabilityCounter == 0)  enemy.move(player.worldToGrid(grid).pos, enemies)
            if (enemy.noMovementAvailable()) numStuck++
            if (enemy.rect.overlaps(player.rect)) player.getHit()
        }
        if (numStuck >= 4) wallLine.removeAll()
    }

    private fun updatePlayer(){
        val direction = Vector2()
        if(Gdx.input.isKeyPressed(Input.Keys.W)) direction.y = 1f
        if(Gdx.input.isKeyPressed(Input.Keys.A)) direction.x = -1f
        if(Gdx.input.isKeyPressed(Input.Keys.S)) direction.y = -1f
        if(Gdx.input.isKeyPressed(Input.Keys.D)) direction.x = 1f
        player.updateSpriteDirection(direction)
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
