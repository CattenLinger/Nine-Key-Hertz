package net.catten.ninekeyhertz.utils

import android.os.SystemClock
import android.view.KeyEvent

class KeyEventWrapper(val event : KeyEvent) {
    /** Translate the key action code */
    enum class Action { DOWN, UP }
    val action by lazy { Action.values()[event.action] }

    override fun toString(): String = buildString {
        append("KeyCode: ${event.keyCode}, Action: ${action}, Flags: ${event.flags}, Down-time: ${event.downTime}")
        if(action == Action.UP) append(", Duration: ${SystemClock.uptimeMillis() - event.downTime}ms")
    }
}