package com.shinonometn.ninekeyhertz.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Binder
import android.os.Build
import android.os.Debug
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.widget.FrameLayout
import android.widget.Toast
import com.shinonometn.ninekeyhertz.R
import com.shinonometn.ninekeyhertz.data.BasicSettings
import com.shinonometn.ninekeyhertz.service.works.EventContext
import com.shinonometn.ninekeyhertz.service.works.EventHandleWorks
import com.shinonometn.ninekeyhertz.utils.EventMappings

class HertzWorkingService : Service() {

    /*
    *
    * Some reference
    *
    * */

    private lateinit var hertzSettings: BasicSettings

    /*
    *
    * For binders
    *
    * */

    inner class LocalBinder : Binder() {
        fun getService() = this@HertzWorkingService
    }

    private val binder = LocalBinder()

    override fun onCreate() {
        hertzSettings = BasicSettings.loadFromJsonFile(this)

        applyFloatWindowSettings()

        if (hertzSettings.disableService)
            toast("Hertz will waiting here until you allow it to work.")
        else
            toast("Hertz comes.")

        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Debug.waitForDebugger()

        toast("Hertz start working.")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        removeOverlayView(getSystemService(Context.WINDOW_SERVICE) as WindowManager)
        hertzSettings.saveToJsonFile(this)

        toast("Hertz stop working.")
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    /*
    *
    * Handle messages from accessibility service
    *
    * */

    val eventFilter = arrayListOf(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER, AccessibilityEvent.TYPE_VIEW_HOVER_EXIT)
    @SuppressLint("SetTextI18n")
    fun handleAccessibilityEvent(eventContext: EventContext) {
        if (hertzSettings.disableService) return

        eventContext.env = this
        val event = eventContext.event
        if (event == null) {
            Log.d(TAG, "Hertz got nothing.")
            return
        }

        if (eventFilter.contains(event.eventType)) return

        Log.d(TAG, "Hertz got message: ${EventMappings.eventOf(event.eventType)} from ${event.className?.toString()}")

        EventHandleWorks.dispatchHandling(eventContext)
    }

    private var latestAccessibilityServiceStatus = AccessibilityServiceStatus.NONE
    val accessibilityServiceStatus = latestAccessibilityServiceStatus
    fun onAccessibilityServiceStatus(status: AccessibilityServiceStatus) {
        latestAccessibilityServiceStatus = status
    }

    /*
    *
    * Some controlling methods
    *
    * */

    fun setShowFloatWindow(show: Boolean): Boolean {
        val oldValue = hertzSettings.showFloatWindow
        try {
            applyFloatWindowSettings()
        } catch (e: Throwable) {
            return oldValue
        }

        hertzSettings.showFloatWindow = show

        return show
    }

    /*
    *
    * Private procedures
    *
    * */

    var focusHintView: FrameLayout? = null
    private val windowParams = WindowManager.LayoutParams().apply {

        width = WindowManager.LayoutParams.MATCH_PARENT
        height = WindowManager.LayoutParams.WRAP_CONTENT

        type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        else
            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT

        flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                .or(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
                .or(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH)

        format = PixelFormat.TRANSLUCENT

        gravity = Gravity.TOP
    }

    private fun applyFloatWindowSettings() {
        val showWindow = hertzSettings.showFloatWindow

        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        if (showWindow) {
            if (focusHintView != null) return
            focusHintView = FrameLayout(this).apply {
                // Load the view
                LayoutInflater.from(this@HertzWorkingService)
                        .inflate(R.layout.hertz_float_window, this)
            }

            // Add it to window manager
            windowManager.addView(focusHintView, windowParams)
            Toast.makeText(this@HertzWorkingService, "View created", Toast.LENGTH_SHORT).show()
        } else {
            removeOverlayView(windowManager)
        }
    }

    private fun removeOverlayView(windowManager: WindowManager) {
        if (focusHintView == null) return
        // remove the view
        windowManager.removeView(focusHintView)
        focusHintView = null
    }

    private fun toast(text: String) {
        if (hertzSettings.verboseToast) Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }


    /*
    *
    * Info
    *
    * */

    companion object {
        private const val TAG = "HertzWorkingService"
    }
}