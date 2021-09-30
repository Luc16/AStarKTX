package com.github.Luc16.AStar.components

import algorithm.GridNode
import algorithm.Heap
import algorithm.HeapNode
import algorithm.Position
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.TimeUtils
import com.github.Luc16.AStar.screens.SIZE_X
import com.github.Luc16.AStar.screens.SIZE_Y
import ktx.graphics.use
import java.util.*
import kotlin.concurrent.schedule
import kotlin.math.abs

class GameGrid(
    private val sizeX: Int,
    private val sizeY: Int,
    color: Color
) {
    val grid: List<List<Node>> = List(sizeY){ i ->
        List(sizeX) {j ->
            Node(Position(i, j), sizeX, sizeY, color)
        }
    }

    fun getNodeInGrid(point: Position): Node{
        return grid[point.line][point.col]
    }

    private fun calculateHCost(node: Node, end: Position): Int {
        val distX: Int = abs(end.line - node.pos.line)
        val distY: Int = abs(end.col - node.pos.col)
        return if (distX > distY) distX*10 + distY*4 else distX*4 + distY*10
    }

    private fun Node.forEachNeighbor(func: (Node, Int, Int) -> Unit){
        for (i in -1..1){
            for (j in -1..1){
                if ((i == 0 && j == 0)) continue

                if( i + this.pos.line in 0 until sizeY &&
                    j + this.pos.col in 0 until  sizeX) {
                    func(grid[i + this.pos.line][j + this.pos.col], i, j)
                }
            }
        }
    }

    fun shortestPath(start: Position, end: Position): Boolean{

        val open = Heap<Node>(sizeX*sizeY)
        open.add(getNodeInGrid(start))

        while (!open.isEmpty()) {

            val current = open.pop()
            current.isClosed = true

            if (current.pos.col == end.col && current.pos.line == end.line){
                paintOpen()
                paintPath(start, end)
                return true
            }

            current.forEachNeighbor { node, i, j ->
                if (!node.isTraversable || node.isClosed) return@forEachNeighbor
                if ( (i != 0 && j != 0 &&
                            !grid[current.pos.line][j + current.pos.col].isTraversable &&
                            !grid[i + current.pos.line][current.pos.col].isTraversable)) return@forEachNeighbor

                val prevGCost = node.gCost
                val newGCost: Int = current.gCost + if (j == 0 || i == 0) 10 else 14

                if (newGCost < node.gCost || node.gCost == 0){
                    node.gCost = newGCost
                    node.parent = current
                    if (prevGCost == 0){
                        node.hCost = calculateHCost(node, end)
                        open.add(node)
                    } else {
                        open.updateItem(node)
                    }

                }

            }

        }
        paintOpen()
        return false
    }

    fun animatedAstar(start: Position, end: Position, open: Heap<Node>): Heap<Node> {
        if (!open.isEmpty()) {
            val current = open.pop()
            current.isClosed = true

            if (current.pos.col == end.col && current.pos.line == end.line){
                paintPath(start, end)
                return Heap<Node>(sizeX*sizeY)
            }

            current.forEachNeighbor { node, i, j ->
                if (!node.isTraversable || node.isClosed) return@forEachNeighbor
                if ( (i != 0 && j != 0 &&
                            !grid[current.pos.line][j + current.pos.col].isTraversable &&
                            !grid[i + current.pos.line][current.pos.col].isTraversable)) return@forEachNeighbor

                val prevGCost = node.gCost
                val newGCost: Int = current.gCost + if (j == 0 || i == 0) 10 else 14

                if (newGCost < node.gCost || node.gCost == 0){
                    node.gCost = newGCost
                    node.parent = current
                    if (prevGCost == 0){
                        node.hCost = calculateHCost(node, end)
                        open.add(node)
                    } else {
                        open.updateItem(node)
                    }

                }

            }
            open.forEach { node ->
                if(!node.isClosed)node.color = Color.BLACK
            }
            current.color = Color.GREEN
            getNodeInGrid(end).color = Color.WHITE
            getNodeInGrid(start).color = Color.WHITE
        }
        else {
            paintOpen()
            return Heap<Node>(sizeX*sizeY)
        }
        return open
    }

    private fun paintOpen(){
        grid.forEach { line ->
            line.forEach { node ->
                if (node.isClosed)
                    node.color = Color.GREEN
            }
        }
    }

    private fun paintPath(start: Position, end: Position) {
        var node: Node? = getNodeInGrid(end)
        while (node != null){
            node.color = Color.WHITE
            node = node.parent as Node?
        }
//        getNodeInGrid(start).color = Color.WHITE
//        getNodeInGrid(end).color = Color.WHITE
    }

    fun draw(renderer: ShapeRenderer){
        grid.forEach { line ->
            line.forEach { node ->
                node.draw(renderer)
            }
        }
    }

    fun resetPath(){
        grid.forEach {line ->
            line.forEach {node ->
                node.reset()
            }
        }
    }


}
