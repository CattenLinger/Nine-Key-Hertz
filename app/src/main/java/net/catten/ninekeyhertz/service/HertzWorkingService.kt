package net.catten.ninekeyhertz.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.Toast
import net.catten.ninekeyhertz.service.works.EventContext
import net.catten.ninekeyhertz.service.works.EventHandleWorks
import net.catten.ninekeyhertz.R
import net.catten.ninekeyhertz.data.*
import net.catten.ninekeyhertz.utils.*

class HertzWorkingService : Service() {

    /*
    *
    * Some reference
    *
    * */

    private val hertzSettings = BasicSettings()
    private val listenerSettings = ListenerSettings()

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

//        applyFloatWindowSettings()

//        if (hertzSettings.disableService)
//            toast("Hertz will waiting here until you allow it to work.")
//        else
//            toast("Hertz comes.")

        toast("Hertz comes.")

        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        Debug.waitForDebugger()

        toast("Hertz start working.")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
//        removeOverlayView(getSystemService(Context.WINDOW_SERVICE) as WindowManager)

//        hertzSettings.saveToFile(this)

        toast("Hertz stop working.")
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    /*
    *
    * Handle messages from accessibility service
    *
    * */

    private val eventFilter by lazy { listenerSettings.eventListenerTarget }

    @SuppressLint("SetTextI18n")
    fun handleAccessibilityEvent(eventContext: EventContext) {
        if (hertzSettings.disableService) return

        eventContext.env = this
        val event = eventContext.event
        if (event == null) {
            Log.d(TAG, "Hertz got nothing.")
            return
        }

        if (eventFilter.contains(EventMappings.eventOf(event.eventType))) return

        Log.d(
            TAG,
            "Hertz got message: ${EventMappings.eventOf(event.eventType)} from ${event.className?.toString()}"
        )

        EventHandleWorks.dispatchHandling(eventContext)
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
    private val windowParams = WindowManagerLayoutParameters {

        fullScreen()
//        params {
//            width = WindowManager.LayoutParams.MATCH_PARENT
//            height = WindowManager.LayoutParams.WRAP_CONTENT
//        }

        typeOverlay()

        useFlags(
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
        )

        layoutParameters {
            format = PixelFormat.TRANSLUCENT
            gravity = Gravity.TOP
        }
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

    private var findResultView: FrameLayout? = null
    fun getFindResultView(): FrameLayout {
        if (findResultView == null) findResultView = FrameLayout(this).apply {
            LayoutInflater.from(this@HertzWorkingService)
                .inflate(R.layout.hertz_found_result_mask, this)
        }

        return findResultView!!
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