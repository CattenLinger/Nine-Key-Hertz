package net.catten.ninekeyhertz.service.works

import android.view.accessibility.AccessibilityEvent
import net.catten.ninekeyhertz.service.HertzAccessibilityService
import net.catten.ninekeyhertz.service.HertzWorkingService

data class EventContext(val event : AccessibilityEvent?, val context : HertzAccessibilityService, var env : HertzWorkingService? = null)