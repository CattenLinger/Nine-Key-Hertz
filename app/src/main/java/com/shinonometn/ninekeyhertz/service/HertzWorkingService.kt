package com.shinonometn.ninekeyhertz.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.Toast
import com.shinonometn.ninekeyhertz.R
import com.shinonometn.ninekeyhertz.service.works.EventContext
import com.shinonometn.ninekeyhertz.service.works.EventHandleWorks
import com.shinonometn.ninekeyhertz.utils.EventMappings
import kotlinx.android.synthetic.main.hertz_float_window.view.*

class HertzWorkingService : Service() {

    /*
    *
    * For binders
    *
    * */

    private val binder = LocalBinder()

    override fun onCreate() {

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.SYSTEM_ALERT_WINDOW) == PackageManager.PERMISSION_GRANTED) {
            createOverlayView()
        }

        Toast.makeText(this,"Hertz comes.",Toast.LENGTH_SHORT).show()
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(applicationContext, "Hertz start working.", Toast.LENGTH_LONG).show()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Toast.makeText(this,"Hertz stop working.", Toast.LENGTH_LONG).show()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    inner class LocalBinder : Binder() {
        fun getService() = this@HertzWorkingService
    }

    /*
    *
    * Handle messages from accessibility service
    *
    * */

    @SuppressLint("SetTextI18n")
    fun handleAccessibilityEvent(eventContext: EventContext) {
        val event = eventContext.event
        if (event == null) {
            Log.d(TAG, "Hertz got nothing.")
            return
        }

        Log.d(TAG, "Hertz got message: ${EventMappings.eventOf(event.eventType)} from ${event.className?.toString()}")

        EventHandleWorks.dispatchHandling(eventContext)?.run dispatch@{
            frameLayout?.run {
                messageLabel.text = getString(R.string.hertz_window_hint_prefix) + this@dispatch
            }
        }

    }

    /*
    *
    * Private procedures
    *
    * */

    private var frameLayout: FrameLayout? = null
    private fun createOverlayView() {
//        Debug.waitForDebugger()
        frameLayout = FrameLayout(this).apply {
            // Load the view
            LayoutInflater.from(this@HertzWorkingService)
                    .inflate(R.layout.hertz_float_window, this)
        }

        // Add it to window manager
        (getSystemService(Context.WINDOW_SERVICE) as WindowManager).addView(frameLayout, WindowManager.LayoutParams().apply {

            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.WRAP_CONTENT

            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT

            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE.or(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)

            format = PixelFormat.TRANSLUCENT

            gravity = Gravity.TOP
        })

        Toast.makeText(this@HertzWorkingService, "View created", Toast.LENGTH_SHORT).show()
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