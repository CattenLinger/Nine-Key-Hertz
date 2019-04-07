package com.shinonometn.ninekeyhertz.data

import com.shinonometn.ninekeyhertz.utils.EventMappings

class ListenerSettings : NamedSettings {
    override val name = "listeners"

    enum class AppListFilterMode {
        BLACKLIST,
        WHITELIST,
        NOT_WORKING
    }
    val eventListenerTarget = HashSet<EventMappings.Event>()

    val appList = HashSet<String>()
    var appListFilterMode = AppListFilterMode.NOT_WORKING


}