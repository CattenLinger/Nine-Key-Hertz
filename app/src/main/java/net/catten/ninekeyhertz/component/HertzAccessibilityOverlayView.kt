package net.catten.ninekeyhertz.component

import android.content.Context
import android.graphics.PixelFormat
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import net.catten.ninekeyhertz.R
import net.catten.ninekeyhertz.databinding.HertzFloatWindowBinding
import net.catten.ninekeyhertz.utils.*
import java.util.concurrent.atomic.AtomicBoolean

class HertzAccessibilityOverlayView(private val context: Context) {
    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    private val isWindowInstalledRef = AtomicBoolean(false)
    val isWindowInstalled: Boolean get() = isWindowInstalledRef.get()

    /* The view */
    private val viewFrame by lazy {
        LinearLayout(context).also { LayoutInflater.from(context).inflate(R.layout.hertz_float_window, it) }
    }
    private val binding by lazy { HertzFloatWindowBinding.bind(viewFrame) }

    fun updateView(modifier: HertzFloatWindowBinding.() -> Unit): Boolean {
        if (!isWindowInstalled) return false
        binding.apply(modifier)
        return true
    }

    fun installWindow() {
        if (isWindowInstalledRef.getAndSet(true)) return
        windowManager.addView(viewFrame, overlayWindowParameter)
        Log.d(TAG, "Overlay view putted to window manager.")
    }

    fun disposeWindow() {
        if (!isWindowInstalledRef.getAndSet(false)) return
        windowManager.removeView(viewFrame)
        Log.d(TAG, "Overlay view removed from window manager.")
    }

    companion object {
        private const val TAG = "HertzOverlayView"

        private val overlayWindowParameter = WindowManagerLayoutParameters {
//            fullScreen()
            fullWidth(); fullHeight()
            typeOverlay()

            useFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
            )

            layoutParameters {
                windowAnimations = android.R.style.Animation_Toast
                format = PixelFormat.TRANSLUCENT
                gravity = Gravity.TOP
            }
        }
    }
}