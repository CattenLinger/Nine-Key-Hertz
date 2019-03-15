package com.shinonometn.ninekeyhertz.service.works

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Debug
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.shinonometn.ninekeyhertz.node.NodeWrapper
import kotlinx.android.synthetic.main.hertz_float_window.view.*

object EventHandleWorks {

    const val TAG = "EventHandleWorks"

    private fun readTree(nodeInfo: AccessibilityNodeInfo?, level: Int): String {
        if (nodeInfo == null) return "Empty node info"
        val sb = StringBuilder()
        val childCount = nodeInfo.childCount

        for (i in 0..level) sb.append("\t")
        sb.append(if (childCount == 0 || level == 0) " * " else "|- ")

        sb.append("${nodeInfo.className} ")

        sb.append(readAreaInfo(nodeInfo))

        val text = nodeInfo.text
        if (text != null) sb.append("[text: $text]")

        sb.append("\n")

        if (childCount > 0) {
            for (i in 0 until childCount) {
                val child = nodeInfo.getChild(i)
                sb.append(readTree(child, level + 1))
            }
        }

        nodeInfo.recycle()
        return sb.toString()
    }

    private fun readAreaInfo(nodeInfo: AccessibilityNodeInfo): String {
        return try {
            val rect = Rect()
            nodeInfo.getBoundsInScreen(rect)
            "[${rect.top},${rect.right},${rect.bottom},${rect.left}]"
        } catch (ignore: Throwable) {
            "[-,-,-,-]"
        }
    }

    @SuppressLint("SetTextI18n")
    fun dispatchHandling(eventContext: EventContext) {
        val event = eventContext.event ?: return

        Debug.waitForDebugger()
        when (event.eventType) {

            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                eventContext.env?.focusHintView?.run {
                    windowNameLabel.text = "C@WINSTAT: ${event.className}"
                }
            }

            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                try {
                    val nodeInfo = event.source ?: return

                    val nodeWrapper = NodeWrapper(nodeInfo)

                    val parentInfo = StringBuilder().apply {
                        val iterator = nodeWrapper.parentNodes.iterator()
                        while (iterator.hasNext()) {
                            val item = iterator.next()
                            append(if (iterator.hasNext()) "   " else "-> ").append(item.className).append(" ${readAreaInfo(item)}").append(" ")
                            append("\n")
                        }
                    }.toString()

                    val childrenInfo = nodeWrapper.run {
                        fun loadNodeInfo(wrapper: NodeWrapper, level: Int = 0) : String {
                            val sb = StringBuilder()
                            val childrenNodes = wrapper.childrenNodes
                            for (i in 0 until level) sb.append("  ")
                            if(level != 0) sb.append("  |-") else sb.append("   ")
                            sb.append(if (childrenNodes.isEmpty()) " * " else "[+]")
                            sb.append(wrapper.type).append(" ").append(readAreaInfo(wrapper.currentNode)).append("\n")

                            if(!childrenNodes.isEmpty()) {
                                childrenNodes.map {
                                    sb.append(loadNodeInfo(it,level + 1))
                                }
                            }
                            return sb.toString()
                        }
                        loadNodeInfo(this)
                    }

                    Log.d(TAG, StringBuilder()
                            .append("Component Tree Report (from package ${event.packageName}) : \n")
                            .append("Parents: \n")
                            .append("$parentInfo\n")
                            .append("Children:\n")
                            .append("$childrenInfo\n")
                            .toString())

                } catch (ignored: Throwable) {
                    Log.e(TAG, "Could not get node info", ignored)
                }

            }

            AccessibilityEvent.TYPE_VIEW_SELECTED,
            AccessibilityEvent.TYPE_VIEW_FOCUSED -> {
                eventContext.env?.focusHintView?.let {
                    val s = try {
                        val bounds = Rect()
                        event.source.getBoundsInScreen(bounds)
                        "@[${bounds.left},${bounds.right},${bounds.top},${bounds.bottom}], Focused: ${event.source.className}"
                    } catch (ignored: Throwable) {
                        "unknown"
                    }

                    it.messageLabel.text = "Focused: $s"
                }
            }

//            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
//                " View ${event.packageName}"
//            }

            else -> null
        }
    }
}