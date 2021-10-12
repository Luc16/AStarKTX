package com.github.Luc16.AStar

import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.github.Luc16.AStar.screens.*
import ktx.app.KtxGame
import ktx.log.Logger
import ktx.log.logger

const val WIDTH = 1280*5/4
const val HEIGHT = 720*5/4

const val SIZE_X = WIDTH/12
const val SIZE_Y = HEIGHT/12

const val SQ_SIZE_X = WIDTH/SIZE_X.toFloat()
const val SQ_SIZE_Y = HEIGHT/SIZE_Y.toFloat()

private val LOG: Logger = logger<AStar>()

class AStar: KtxGame<CustomScreen>() {
    val renderer: ShapeRenderer by lazy { ShapeRenderer() }
    val font: BitmapFont by lazy { BitmapFont() }
    val batch: Batch by lazy { SpriteBatch() }

    override fun create() {
        font.data.scale(2f)
        Gdx.app.logLevel = LOG_DEBUG
        LOG.debug { "Create game instance" }
        addScreen(Screen1(this))
        addScreen(AlgorithmScreen(this, Color.LIGHT_GRAY))
        addScreen(GameScreen(this, Color.NAVY))
        addScreen(MenuScreen(this))
        setScreen<GameScreen>()
    }

    override fun dispose() {
        super.dispose()
        renderer.dispose()
        batch.dispose()
        font.dispose()
    }
}