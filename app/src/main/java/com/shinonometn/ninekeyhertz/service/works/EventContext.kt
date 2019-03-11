package com.shinonometn.ninekeyhertz.service.works

import android.view.accessibility.AccessibilityEvent
import com.shinonometn.ninekeyhertz.service.HertzAccessibilityService

data class EventContext(val event : AccessibilityEvent?, val context : HertzAccessibilityService)