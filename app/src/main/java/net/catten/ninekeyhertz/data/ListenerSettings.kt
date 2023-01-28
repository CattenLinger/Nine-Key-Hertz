package net.catten.ninekeyhertz.data

import net.catten.ninekeyhertz.utils.EventMappings

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