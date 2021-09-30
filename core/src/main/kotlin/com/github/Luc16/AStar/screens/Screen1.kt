package com.github.Luc16.AStar.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.FitViewport
import com.github.Luc16.AStar.AStar
import ktx.app.clearScreen
import ktx.graphics.use
import ktx.log.Logger
import ktx.log.logger

private val LOG: Logger = logger<Screen1>()

data class Santa(val rect: Rectangle = Rectangle(40f, 80f, 80f, 80f))
data class ChristmasGift(
    val rect: Rectangle = Rectangle((40..1200).random().toFloat(), 720f, 60f, 60f),
    var color: Color = Color.GREEN
)

class Screen1(game: AStar): CustomScreen(game) {

    override fun show() {
        LOG.debug { "First screen is now active" }
    }
    private val player = Santa()
    private val gifts = mutableListOf<ChristmasGift>()


    override fun render(delta: Float) {
        handleInput()
        logic()
        draw()
    }

    private fun handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.rect.x -= 5f
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.rect.x += 5f
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) game.setScreen<AlgorithmScreen>()
    }

    private fun logic() {
        val toRemove = mutableListOf<Int>()
        if (Math.random() > 0.95) {
            gifts.add(ChristmasGift())
        }
        val mouse = Vector2(Gdx.input.x.toFloat(), 720 - Gdx.input.y.toFloat())
        gifts.forEachIndexed { idx, gift ->
            gift.rect.y -= 2f
            if(gift.rect.overlaps(player.rect)) toRemove.add(idx)
            if (gift.rect.contains(mouse))
                gift.color = Color.BLUE
        }
        toRemove.forEach { idxToRemove ->
            gifts.removeAt(idxToRemove)
        }
        println("gifts size: ${gifts.size}")
    }
    private fun ShapeRenderer.drawRect(rect: Rectangle){
        this.rect(rect.x, rect.y, rect.width, rect.height)
    }
    private fun draw() {
        clearScreen(0f, 0f, 0f, 0f)

        renderer.use(ShapeRenderer.ShapeType.Filled) {
            gifts.forEach { gift ->
                renderer.color = gift.color
                renderer.drawRect(gift.rect)
            }
            renderer.color = Color.RED
            renderer.drawRect(player.rect)
        }

    }


}
