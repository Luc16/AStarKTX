package com.github.Luc16.AStar.components

import algorithm.Heap
import algorithm.Position
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import kotlin.math.abs

class GameGrid(
    private val sizeX: Int,
    private val sizeY: Int,
    color: Color
) {
    val grid: List<List<Node>> = List(sizeY){ i ->
        List(sizeX) {j ->
            Node(Position(i, j), color)
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
    
    fun shortestPath(start: Position, end: Position, paint: Boolean): List<Vector2> {
        val open = Heap<Node>(sizeX*sizeY)
        open.add(getNodeInGrid(start))

        while (!open.isEmpty()) {

            val current = open.pop()
            current.isClosed = true

            if (current.pos.col == end.col && current.pos.line == end.line){
                if (paint) paintOpen()
                return paintPath(end, paint)
            }

            val jBlock = mutableListOf<Int>()
            val iBlock = mutableListOf<Int>()
            current.forEachNeighbor(this) { node, i, j ->
                if (i == 0 && !node.isTraversable) jBlock.add(j)
                else if (j == 0 && !node.isTraversable) iBlock.add(i)
            }

            current.forEachNeighbor(this) { node, i, j ->
                if (!node.isTraversable || node.isClosed) return@forEachNeighbor
                if (i in iBlock || j in jBlock) return@forEachNeighbor

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
        if (paint) paintOpen()
        return listOf(Vector2(0f, 0f))
    }

    fun animatedAstar(start: Position, end: Position, open: Heap<Node>): Heap<Node> {
        if (!open.isEmpty()) {
            val current = open.pop()
            current.isClosed = true

            if (current.pos.col == end.col && current.pos.line == end.line){
                paintPath(end, true)
                return Heap(sizeX*sizeY)
            }

            current.forEachNeighbor(this) { node, i, j ->
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
            return Heap(sizeX*sizeY)
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

    private fun paintPath(end: Position, paint: Boolean): List<Vector2> {
        var node: Node? = getNodeInGrid(end)
        val directions = mutableListOf<Vector2>()
        while (node != null){
            node.parent?.let { parent ->
                node?.let { node ->
                    directions.add(Vector2((node.pos.col - parent.pos.col).toFloat(), (parent.pos.line - node.pos.line).toFloat()))
                }
            }
            if (paint) node.color = Color.WHITE
            node = node.parent
        }
        return directions.reversed()
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
