package net.catten.ninekeyhertz.node

import android.graphics.Rect
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import java.util.*

class NodeWrapper(accessibilityNodeInfo: AccessibilityNodeInfo) {

    // The current node
    val currentNode: AccessibilityNodeInfo = accessibilityNodeInfo
    private var recycledFlag = false
    val isRecycled: Boolean
        get() = recycledFlag

    // The depth of this node, start from 0
    val nodeLevelIndex: Int

    // Get all parents, start from root, end from latest parent
    val parentNodes: Array<AccessibilityNodeInfo>

    // Children
    val childrenNodes: List<NodeWrapper>
        get() = loadChildren(currentNode)

    // Area in screen
    val areaInfo = loadAreaInfo(currentNode)

    val type = currentNode.className

    fun recycle() {
        currentNode.recycle()
        recycledFlag = true
    }

    init {
        parentNodes = initializeParent(currentNode)
        nodeLevelIndex = parentNodes.size

    }

    private fun initializeParent(accessibilityNodeInfo: AccessibilityNodeInfo?): Array<AccessibilityNodeInfo> {
        if (accessibilityNodeInfo == null) return Collections.emptyList<AccessibilityNodeInfo>().toTypedArray()

        val linkedList = LinkedList<AccessibilityNodeInfo>()

        var depth = 0
        var node = accessibilityNodeInfo.parent
        while (node != null) {
            linkedList.addLast(node)
            node = node.parent
            depth++
        }

        return linkedList.toTypedArray()
    }

    private fun loadChildren(nodeInfo: AccessibilityNodeInfo): List<NodeWrapper> {
        val childCount = nodeInfo.childCount
        if (childCount == 0) return Collections.emptyList()

        val list = LinkedList<NodeWrapper>()
        for (i in 0..(childCount - 1)) {
            list.add(NodeWrapper(nodeInfo.getChild(i)))
        }
        return list
    }

    private fun loadAreaInfo(nodeInfo: AccessibilityNodeInfo) : Rect? {
        return try {
            val rect = Rect()
            nodeInfo.getBoundsInScreen(rect)
            rect
        } catch (ignore: Throwable) {
            Log.d(TAG,"Could not get area info for $nodeInfo.", ignore)
            null
        }
    }

    companion object {
        private const val TAG = "NKH-NodeWrapper"
    }
}