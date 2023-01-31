package net.catten.ninekeyhertz.service

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancel
import net.catten.ninekeyhertz.component.HertzAccessibilityDelegate
import net.catten.ninekeyhertz.component.ServiceLifecycleHelper
import net.catten.ninekeyhertz.component.afterCreate
import net.catten.ninekeyhertz.component.beforeDestroy
import net.catten.ninekeyhertz.utils.identifierOfClazz
import java.util.concurrent.Executors

class HertzAccessibilityService : AccessibilityService() {

    /** Lifecycle helper delegate */
    internal val lifecycleHelper = ServiceLifecycleHelper(this)
    override fun onCreate() = lifecycleHelper.hooks.onCreate { Log.d(TAG, "Accessibility service created.") }
    override fun onDestroy() = lifecycleHelper.hooks.onDestroy { Log.d(TAG, "Accessibility service destroyed.") }

    /* Resources */
    private val scope: CoroutineScope by lazy {
        Log.d(TAG, "Creating coroutine scope for works.")
        val executor = Executors.newSingleThreadExecutor()
        Log.d(TAG, "Created executor for coroutine scope.")
        CoroutineScope(executor.asCoroutineDispatcher()).also {
            lifecycleHelper.beforeDestroy {
                Log.d(TAG, "Shutting down coroutine scope.")
                it.cancel(CancellationException("Service is shutting down"))

                Log.d(TAG, "Shutting down executor.")
                executor.shutdown()
            }
        }
    }

    /*
    *
    * Handle the working service
    *
    * */
//    private var workingService: HertzWorkingService? = null
//    private var isWorkingServiceBound = false
//
//    private val workingServiceConnection = object : ServiceConnection {
//        override fun onServiceDisconnected(name: ComponentName?) {
//            isWorkingServiceBound = false
//            onWorkingServiceUnbind()
//        }
//
//        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
//            val binder = service as HertzWorkingService.LocalBinder
//            workingService = binder.getService()
//            isWorkingServiceBound = true
//        }
//    }
//
//    private fun onWorkingServiceUnbind() {
//        Log.d(TAG, "Working service unbound.")
//    }

    /*
    *
    * Accessibility service
    *
    * */

    /*init {
        lifecycleHelper.afterCreate {
            bindService(
                Intent(this, HertzWorkingService::class.java),
                workingServiceConnection,
                Context.BIND_AUTO_CREATE
            )
        }

        lifecycleHelper.beforeDestroy {
            workingService?.onAccessibilityServiceStatus(AccessibilityServiceStatus.DESTROY)
            unbindService(workingServiceConnection)
        }
    }*/

    override fun onServiceConnected() {
        Log.d(TAG, "Connected to Accessibility service.")
//        workingService?.onAccessibilityServiceStatus(AccessibilityServiceStatus.CONNECTED)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "Accessibility service unbind.")
//        workingService?.onAccessibilityServiceStatus(AccessibilityServiceStatus.UNBIND)
        return true
    }

    override fun onRebind(intent: Intent?) {
        Log.d(TAG, "Accessibility service bind.")
//        workingService?.onAccessibilityServiceStatus(AccessibilityServiceStatus.REBIND)
    }

    override fun onInterrupt() {
        Log.d(TAG, "Accessibility service interrupted.")
//        workingService?.onAccessibilityServiceStatus(AccessibilityServiceStatus.INTERRUPT)
    }

    /** Accessibility delegate */
    private val accessibilityDelegate = HertzAccessibilityDelegate(this)
    override fun onAccessibilityEvent(event: AccessibilityEvent) = accessibilityDelegate.onAccessibilityEvent(event)
    override fun onKeyEvent(event: KeyEvent): Boolean = accessibilityDelegate.onKeyEvent(event) { super.onKeyEvent(event) }

    /*
    *
    * private procedure
    *
    * */

    companion object {
        private const val TAG = "HertzAccSrv"

        /** Read system's accessibility settings to find out if NineKeyHertz is enabled. */
        fun isAccessibilitySettingsOn(context: Context): Boolean {
            Log.d(TAG, "Try test if accessibility service is enabled.")
            val serviceName = context.identifierOfClazz<HertzAccessibilityService>().lowercase()
            try {
                val settingValues = with(context.applicationContext) {
                    if (Settings.Secure.getInt(contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED) < 1) return false
                    Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES) ?: return false
                }
                return settingValues.split(":").any { it.lowercase() == serviceName }

//                return TextUtils.SimpleStringSplitter(':').apply { setString(settingValues) }.any { it.equals(serviceName, ignoreCase = true) }

            } catch (e: Settings.SettingNotFoundException) {
                Log.w(TAG, "Accessibility setting not found")
                return false
            }
        }
    }
}