package com.github.Luc16.AStar.utils

import com.github.Luc16.AStar.components.Node

class WallLineNode(val node: Node){
    var prev: WallLineNode? = null
    var next: WallLineNode? = null
}

class WallLine(private val maxSize: Int) {
    private var head: WallLineNode? = null
    private var tail: WallLineNode? = null
    var size = 0

    private fun remove(lineNode: WallLineNode){
        val prev = lineNode.prev
        val next = lineNode.next
        if (next != null) {
            next.prev = prev
        } else {
            tail = prev
        }
        if (prev != null) {
            prev.next = next
        } else {
            head = next
        }
        lineNode.prev = null
        lineNode.next = null
        lineNode.node.becomeTraversable()
        size--
    }

    fun updateLine(){
        var lineNode = if (head != null) head else return
        while (lineNode != null) {
            val nextLineNode = lineNode.next
            if (lineNode.node.isTraversable) remove(lineNode)
            lineNode = nextLineNode
        }

    }

    fun add(node: Node){
        if (size == maxSize){
            tail?.let {
                remove(it)
            }
        }
        val lineNode = WallLineNode(node)
        if (tail == null) tail = lineNode
        if (head != null){
            head?.prev = lineNode
            lineNode.next = head
        }
        head = lineNode
        lineNode.node.becomeWall()
        size++
    }

    fun removeAll(){
        var lineNode = if (head != null) head else return
        while (lineNode != null) {
            val nextLineNode = lineNode.next
            remove(lineNode)
            lineNode = nextLineNode
        }
    }
}