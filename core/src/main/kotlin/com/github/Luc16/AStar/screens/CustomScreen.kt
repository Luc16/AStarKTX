package com.github.Luc16.AStar.screens

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.github.Luc16.AStar.AStar
import ktx.app.KtxScreen

abstract class CustomScreen(
    val game: AStar,
    val batch: Batch = game.batch,
    val renderer: ShapeRenderer = game.renderer,
    val font: BitmapFont = game.font,
) : KtxScreen