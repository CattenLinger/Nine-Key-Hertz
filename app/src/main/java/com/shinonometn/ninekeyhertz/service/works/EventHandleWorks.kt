package com.shinonometn.ninekeyhertz.service.works

import android.view.accessibility.AccessibilityEvent

object EventHandleWorks {

    fun dispatchHandling(eventContext: EventContext): String? {
        val event = eventContext.event ?: return null

        return when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
            AccessibilityEvent.TYPE_WINDOWS_CHANGED,
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                " Window ${event.packageName}"
            }

            AccessibilityEvent.TYPE_VIEW_CLICKED -> " View ${event.packageName}"

            else -> null
        }
    }
}