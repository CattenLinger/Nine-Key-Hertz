package net.catten.ninekeyhertz.component

import android.app.Service
import java.util.*

class ServiceLifecycleHelper(private val wrapped : Service) {
    private val events = mutableMapOf<Int, LinkedList<() -> Unit>>()
    enum class EventKey { BeforeCreate, AfterCreate, BeforeBind, AfterBind, BeforeDestroy, AfterDestroy, BeforeStart, AfterStart }

    @Synchronized
    fun registerListenerFor(key : EventKey, action : () -> Unit) : Pair<EventKey, () -> Unit> {
        val theList = events[key.ordinal] ?: LinkedList<() -> Unit>().also { events[key.ordinal] = it }
        theList.add(action)
        return key to action
    }

    @Synchronized
    fun removeListenerFor(key : EventKey, action: () -> Unit) : Boolean {
        val theList = events[key.ordinal] ?: return false
        return theList.remove(action)
    }

    /** Hooks are reverse-controls for service lifecycles. */
    interface Hooks {
        fun onCreate(then : () -> Unit = {})
        fun onBind(then : () -> Unit = {})
        fun onDestroy(then : () -> Unit = {})
        fun onStartCommand(then : () -> Unit = {})
    }
    private class HooksImpl(private val host : ServiceLifecycleHelper) : Hooks {
        private fun invokeEventGroup(key : EventKey) = host.events[key.ordinal]?.takeIf { it.isNotEmpty() }?.forEach { it() }

        override fun onCreate(then: () -> Unit) {
            invokeEventGroup(EventKey.BeforeCreate)
            then()
            invokeEventGroup(EventKey.AfterCreate)
        }

        override fun onBind(then: () -> Unit) {
            invokeEventGroup(EventKey.BeforeBind)
            then()
            invokeEventGroup(EventKey.AfterBind)
        }

        override fun onDestroy(then: () -> Unit) {
            invokeEventGroup(EventKey.BeforeDestroy)
            then()
            invokeEventGroup(EventKey.AfterDestroy)
        }

        override fun onStartCommand(then: () -> Unit) {
            invokeEventGroup(EventKey.BeforeStart)
            then()
            invokeEventGroup(EventKey.AfterStart)
        }
    }
    val hooks : Hooks = HooksImpl(this)
}

/* Helpers */

/** Register an action to BeforeCreate hook */
fun ServiceLifecycleHelper.beforeCreate(action: () -> Unit) = registerListenerFor(ServiceLifecycleHelper.EventKey.BeforeCreate, action)

/** Register an action to AfterCreate hook */
fun ServiceLifecycleHelper.afterCreate(action: () -> Unit) = registerListenerFor(ServiceLifecycleHelper.EventKey.AfterCreate, action)

/** Register an action to BeforeBind hook */
fun ServiceLifecycleHelper.beforeBind(action: () -> Unit) = registerListenerFor(ServiceLifecycleHelper.EventKey.BeforeBind, action)

/** Register an action to AfterBind hook */
fun ServiceLifecycleHelper.afterBind(action: () -> Unit) = registerListenerFor(ServiceLifecycleHelper.EventKey.AfterBind, action)

/** Register an action to BeforeDestroy hook */
fun ServiceLifecycleHelper.beforeDestroy(action: () -> Unit) = registerListenerFor(ServiceLifecycleHelper.EventKey.BeforeDestroy, action)

/** Register an action to AfterDestroy hook */
fun ServiceLifecycleHelper.afterDestroy(action: () -> Unit) = registerListenerFor(ServiceLifecycleHelper.EventKey.AfterDestroy, action)

/** Register an action to BeforeStart hook */
fun ServiceLifecycleHelper.beforeStart(action: () -> Unit) = registerListenerFor(ServiceLifecycleHelper.EventKey.BeforeStart, action)

/** Register an action to AfterStart hook */
fun ServiceLifecycleHelper.afterStart(action: () -> Unit) = registerListenerFor(ServiceLifecycleHelper.EventKey.AfterStart, action)