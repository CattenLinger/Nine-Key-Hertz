package net.catten.ninekeyhertz.data

class BasicSettings : NamedSettings {

    override val name = "settings"

    var disableService: Boolean = false
    var showFloatWindow: Boolean = true
    var verboseToast: Boolean = true
}