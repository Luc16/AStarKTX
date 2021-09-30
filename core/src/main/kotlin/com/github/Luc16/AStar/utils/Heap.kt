package algorithm

import com.github.Luc16.AStar.components.Node
import javax.lang.model.element.Element

private const val MAX_SIZE_ERROR = "At max size"
private const val EMPTY_HEAP_ERROR = "Heap is empty"

class Heap<T: HeapNode<T>>(
    private val maxHeapSize: Int
) {
    private val heapArray = Array<Any?>(maxHeapSize) { null }
    var currentSize = 0

    private fun getParentIndex(element: T): Int = (element.heapIdx - 1)/2

    private fun getChildrenIndex(element: T, childNum: Int): Int = 2*element.heapIdx+childNum

    fun peek(): T = if (currentSize > 0) (heapArray[0] as T) else error(EMPTY_HEAP_ERROR)

    fun isEmpty(): Boolean = currentSize <= 0

    private fun T.swap(element: T) {
        heapArray[heapIdx] = heapArray[element.heapIdx].also { heapArray[element.heapIdx] = heapArray[heapIdx] }
        heapIdx = element.heapIdx.also { element.heapIdx = heapIdx }
    }

    private fun sortUp(element: T){
        while (true){
            val parentIdx = getParentIndex(element)
            if ((heapArray[element.heapIdx] as T) < (heapArray[parentIdx] as T) || element.heapIdx == 0) return
            element.swap((heapArray[parentIdx] as T))
        }
    }

    fun add(element: T){
        if (currentSize >= maxHeapSize) error(MAX_SIZE_ERROR)
        heapArray[currentSize] = element
        element.heapIdx = currentSize
        sortUp(element)
        currentSize++
    }

    private fun sortDown(element: T){
        while (true) {
            val idxChild1 = getChildrenIndex(element, 1)
            var best = if (idxChild1 < currentSize) idxChild1 else return
            val idxChild2 = getChildrenIndex(element, 2)
            if (idxChild2 < currentSize)
                best = if ((heapArray[idxChild1] as T) > (heapArray[idxChild2] as T)) idxChild1 else idxChild2

            if (element > (heapArray[best] as T) || element.heapIdx >= currentSize - 1) return
            element.swap((heapArray[best] as T))

        }
    }

    fun pop(): T {
        val element = peek()
        currentSize--
        heapArray[0] = heapArray[currentSize]
        (heapArray[currentSize] as T).heapIdx = 0
        sortDown((heapArray[currentSize] as T))
        return element
    }
    
    fun updateItem(element: T){
        sortUp(element)
//        sortDown(element)
    }

    override fun toString(): String {
        var str = ""
        heapArray.forEach {
            if (it != null) str += " $it"
        }
        return str
    }

    fun forEach(func: (T) -> Unit){
        heapArray.forEach { element ->
            if (element == null) return
            func(element as T)
        }
    }
}

abstract class HeapNode<T>: Comparable<T>{
    var heapIdx: Int = 0
}