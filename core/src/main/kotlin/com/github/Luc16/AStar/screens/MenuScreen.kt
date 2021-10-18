package com.github.Luc16.AStar.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.FitViewport
import com.github.Luc16.AStar.AStar
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.github.Luc16.AStar.HEIGHT
import com.github.Luc16.AStar.WIDTH
import ktx.app.clearScreen
import ktx.graphics.use

const val TIME = 10

class MenuScreen(game: AStar):CustomScreen(game) {

    private val colors = listOf<Color>(Color.WHITE, Color.YELLOW, Color.BLUE, Color.ORANGE, Color.CORAL, Color.RED, Color.CHARTREUSE, Color.GOLDENROD)
    private var i = 0
    private val file = Gdx.files.local("assets/score.txt")
    private val gameButton = Rectangle(WIDTH/2 - 170f, HEIGHT/2 - 30f, 400f, 60f)
    private var gameButtonColor = Color.VIOLET
    private val algoButton = Rectangle(WIDTH/2 - 170f, HEIGHT/2 - 110f, 400f, 60f)
    private var algoButtonColor = Color.VIOLET

    override fun render(delta: Float) {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (i% TIME == 0){
                if (i/TIME >= colors.size) i = 0
                font.color = colors[i/TIME]
            }
            i++
        }
        val scores = file.readString().split(" ")
        renderer.use(ShapeRenderer.ShapeType.Filled) {
            renderer.color = gameButtonColor
            renderer.rect(gameButton.x, gameButton.y, gameButton.width, gameButton.height)
            renderer.color = algoButtonColor
            renderer.rect(algoButton.x, algoButton.y, algoButton.width, algoButton.height)
        }
        batch.use {
            font.draw(batch, "MENU", WIDTH/2 - 100f, HEIGHT/2 + 200f)
            font.draw(batch, "Best score: ${scores[0]}", WIDTH/2 - 170f, HEIGHT/2 + 150f)
            font.draw(batch, "Your score: ${scores[1]}", WIDTH/2 - 170f, HEIGHT/2 + 100f)
            font.draw(batch, "Game", WIDTH/2 - 30f, HEIGHT/2 + 20f)
            font.draw(batch, "Algorithm", WIDTH/2 - 60f, HEIGHT/2 - 60f)
        }

        val mouse = Vector2(Gdx.input.x.toFloat(), HEIGHT - Gdx.input.y.toFloat())
        when {
            Gdx.input.isKeyPressed(Input.Keys.NUM_1) -> game.setScreen<AlgorithmScreen>()
            Gdx.input.isKeyPressed(Input.Keys.ENTER) -> game.setScreen<GameScreen>()
            gameButton.contains(mouse) -> {
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) game.setScreen<GameScreen>()
                else gameButtonColor = Color.PINK
            }
            algoButton.contains(mouse) -> {
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) game.setScreen<AlgorithmScreen>()
                else algoButtonColor = Color.PINK
            }
            else -> {
                algoButtonColor = Color.VIOLET
                gameButtonColor = Color.VIOLET
            }

        }

    }
}