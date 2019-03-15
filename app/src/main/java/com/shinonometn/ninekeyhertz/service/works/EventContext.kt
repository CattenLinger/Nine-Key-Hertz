package com.shinonometn.ninekeyhertz.service.works

import android.view.accessibility.AccessibilityEvent
import com.shinonometn.ninekeyhertz.service.HertzAccessibilityService
import com.shinonometn.ninekeyhertz.service.HertzWorkingService

data class EventContext(val event : AccessibilityEvent?, val context : HertzAccessibilityService, var env : HertzWorkingService? = null)