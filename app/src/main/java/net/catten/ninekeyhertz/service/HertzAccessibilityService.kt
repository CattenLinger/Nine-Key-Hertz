package net.catten.ninekeyhertz.service

import android.accessibilityservice.AccessibilityService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import net.catten.ninekeyhertz.R
import net.catten.ninekeyhertz.service.works.EventContext

class HertzAccessibilityService : AccessibilityService() {

    /*
    *
    * Handle the working service
    *
    * */
    private var workingService: HertzWorkingService? = null
    private var isWorkingServiceBound = false

    private val workingServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            isWorkingServiceBound = false
            onWorkingServiceUnbind()
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as HertzWorkingService.LocalBinder
            workingService = binder.getService()
            isWorkingServiceBound = true
        }
    }

    private fun onWorkingServiceUnbind() {
        Log.d(TAG, "Working service unbound.")
    }

    /*
    *
    * Accessibility service
    *
    * */

    override fun onCreate() {
        Log.d(TAG, "Accessibility service created.")
        Intent(this, HertzWorkingService::class.java).also {
            bindService(it, workingServiceConnection, Context.BIND_AUTO_CREATE)
        }

    }

    override fun onServiceConnected() {
        Log.d(TAG, "Connected to Accessibility service.")
        workingService?.onAccessibilityServiceStatus(AccessibilityServiceStatus.CONNECTED)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "Accessibility service unbind.")
        workingService?.onAccessibilityServiceStatus(AccessibilityServiceStatus.UNBIND)
        return true
    }

    override fun onRebind(intent: Intent?) {
        Log.d(TAG, "Accessibility service bind.")
        workingService?.onAccessibilityServiceStatus(AccessibilityServiceStatus.REBIND)
    }

    override fun onInterrupt() {
        Log.d(TAG, "Accessibility service interrupted.")
        workingService?.onAccessibilityServiceStatus(AccessibilityServiceStatus.INTERRUPT)
    }

    override fun onDestroy() {
        workingService?.onAccessibilityServiceStatus(AccessibilityServiceStatus.DESTROY)
        unbindService(workingServiceConnection)
        Log.d(TAG, "Accessibility service destroyed.")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (!isWorkingServiceBound) {
            Log.d(TAG, "Hertz wasn't here, message will be drop")
            return
        }
        workingService?.handleAccessibilityEvent(EventContext(event, this))
    }

    /*
    *
    * private procedure
    *
    * */


    /*
    *
    * Helper methods
    *
    * */

    companion object {
        private const val TAG = "HertzAccessSrv"

        fun isAccessibilitySettingsOn(context: Context): Boolean {
            Log.d(TAG,"Try test if accessibility service is enabled.")

            val service = "net.catten.ninekeyhertz/net.catten.ninekeyhertz.service.HertzAccessibilityService"

            try {
                if (Settings.Secure.getInt(context.applicationContext.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED) < 1)
                    return false

                val settingValues = Settings.Secure.getString(context.applicationContext.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
                        ?: return false

                return TextUtils.SimpleStringSplitter(':')
                        .apply { setString(settingValues) }
                        .any { it.equals(service, ignoreCase = true) }

            } catch (e: Settings.SettingNotFoundException) {
                Log.i(TAG, "Accessibility setting not found")
                return false
            }
        }
    }
}