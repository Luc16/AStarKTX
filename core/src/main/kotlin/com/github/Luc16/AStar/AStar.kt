package com.github.Luc16.AStar

import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.github.Luc16.AStar.screens.AlgorithmScreen
import com.github.Luc16.AStar.screens.CustomScreen
import com.github.Luc16.AStar.screens.GameScreen
import com.github.Luc16.AStar.screens.Screen1
import ktx.app.KtxGame
import ktx.log.Logger
import ktx.log.logger

const val WIDTH = 1280
const val HEIGHT = 720

const val SIZE_X = 128
const val SIZE_Y = 72

private val LOG: Logger = logger<AStar>()

class AStar: KtxGame<CustomScreen>() {
    val renderer: ShapeRenderer by lazy { ShapeRenderer() }

    override fun create() {
        Gdx.app.logLevel = LOG_DEBUG
        LOG.debug { "Create game instance" }
        addScreen(Screen1(this))
        addScreen(AlgorithmScreen(this, Color.LIGHT_GRAY))
        addScreen(GameScreen(this, Color.NAVY))
        setScreen<GameScreen>()
    }

    override fun dispose() {
        super.dispose()
        renderer.dispose()
    }
}