package com.github.Luc16.AStar.components

import algorithm.GridNode
import algorithm.HeapNode
import algorithm.Position
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.github.Luc16.AStar.*


class Node(val pos: Position, var color: Color): HeapNode<Node>() {
    var parent: Node? = null
    var hCost = 0
    var gCost = 0
    private val fCost: Int
        get() {
            return gCost + hCost
        }
    var isTraversable = true
    var isClosed = false

    val rect = Rectangle(pos.col* SQ_SIZE_X, HEIGHT - (pos.line + 1)*SQ_SIZE_Y,
        SQ_SIZE_X, SQ_SIZE_Y)
    val defaultColor = color
    private val wallColor: Color = Color.BROWN

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

    fun becomeTraversable(){
        isTraversable = true
        color = defaultColor
    }

    fun becomeWall(){
        isTraversable = false
        color = wallColor
    }
}