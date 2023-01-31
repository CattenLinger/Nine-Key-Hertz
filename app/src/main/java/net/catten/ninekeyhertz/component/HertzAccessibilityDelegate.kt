package net.catten.ninekeyhertz.component

import android.util.Log
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import net.catten.ninekeyhertz.service.HertzAccessibilityService
import net.catten.ninekeyhertz.utils.KeyEventWrapper
import kotlin.properties.Delegates.observable

class HertzAccessibilityDelegate(host: HertzAccessibilityService) {
    private val AccessibilityEvent.isWindowStateChangedEvent: Boolean
        get() = (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED)

    private val viewDelegate by lazy {
        HertzAccessibilityOverlayView(host).also {
            it.installWindow()
            host.lifecycleHelper.beforeDestroy { it.disposeWindow() }
        }
    }

    private var currentApplicationPackageName by observable("Unknown") { _, _, new ->
        viewDelegate.updateView { windowNameLabel.text = new }
    }

    private var currentViewInstanceTypeName by observable("Unknown") { _, _, new ->
        viewDelegate.updateView { messageLabel.text = new }
    }

    fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (!event.isWindowStateChangedEvent) return
        currentApplicationPackageName = event.source?.packageName?.toString() ?: "Unknown"
        currentViewInstanceTypeName = event.className.toString()
        Log.d(TAG, "Hertz got TYPE_WINDOW_STATE_CHANGED from a '${currentApplicationPackageName}/${currentViewInstanceTypeName}' instance.")
        event.recycle()
    }

    fun onKeyEvent(event: KeyEvent, default: (() -> Boolean)? = null): Boolean {
        Log.d(TAG, "Hertz got key. ${KeyEventWrapper(event)}")
        return default?.invoke() ?: false
    }

    companion object {
        private const val TAG = "HertzAccDlgt"
    }
}