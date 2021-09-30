package com.github.Luc16.AStar.components

import algorithm.GridNode
import algorithm.HeapNode
import algorithm.Position
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.github.Luc16.AStar.HEIGHT
import com.github.Luc16.AStar.WIDTH


class Node(val pos: Position, sizeX: Int, sizeY: Int, var color: Color): HeapNode<Node>() {
    var parent: Node? = null
    var hCost = 0
    var gCost = 0
    private val fCost: Int
        get() {
            return gCost + hCost
        }
    var isTraversable = true
    var isClosed = false

    val rect = Rectangle(pos.col*WIDTH/sizeX.toFloat(), HEIGHT - (pos.line + 1)*HEIGHT/sizeY.toFloat(),
        WIDTH/sizeX.toFloat(), HEIGHT/sizeY.toFloat())
    val defaultColor = color

    override fun toString(): String = fCost.toString()

    override fun compareTo(other: Node): Int = if (other.fCost - fCost == 0) other.hCost - hCost else other.fCost - fCost

    fun draw(renderer: ShapeRenderer){
        renderer.color = color
        renderer.rect(rect.x, rect.y, rect.width, rect.height)
    }

    fun reset(){
        if (isTraversable) color = defaultColor
        parent = null
        hCost = 0
        gCost = 0
        isClosed = false
    }
}