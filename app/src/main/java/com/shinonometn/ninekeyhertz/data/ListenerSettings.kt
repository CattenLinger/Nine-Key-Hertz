package com.shinonometn.ninekeyhertz.data

class ListenerSettings : NamedSettings {
    override val name = "listeners"

    val appList : MutableList<String> = ArrayList()
}