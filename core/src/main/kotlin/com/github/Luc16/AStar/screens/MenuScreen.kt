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
import com.badlogic.gdx.utils.viewport.FitViewport
import com.github.Luc16.AStar.AStar
import com.badlogic.gdx.utils.viewport.ScreenViewport
import ktx.app.clearScreen
import ktx.graphics.use

const val TIME = 10

class MenuScreen(game: AStar,
                 private val font: BitmapFont = game.font,
                 private val batch: Batch = game.batch,
):CustomScreen(game) {

    private val colors = listOf<Color>(Color.WHITE, Color.YELLOW, Color.BLUE, Color.ORANGE, Color.CORAL, Color.RED, Color.CHARTREUSE, Color.GOLDENROD)
    private var i = 0

    override fun show() {
        font.data.scale(2f)
    }
    override fun render(delta: Float) {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (i% TIME == 0){
                if (i/TIME >= colors.size) i = 0
                font.color = colors[i/TIME]
            }
            i++
        }
        batch.use {
            font.draw(batch, "EU TE AMOOOOOO", 450f, 380f)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) game.setScreen<AlgorithmScreen>()
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) game.setScreen<GameScreen>()

        super.render(delta)
    }
}