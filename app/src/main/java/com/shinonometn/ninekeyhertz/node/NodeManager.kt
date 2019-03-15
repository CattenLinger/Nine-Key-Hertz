package com.shinonometn.ninekeyhertz.node

import java.util.concurrent.ConcurrentHashMap

class NodeManager {
    private val nodeMap = ConcurrentHashMap<String,Any>()

    private val listenerGroup = HashMap<Int, MutableList<Any>>()

    companion object {
        private val LISTENER_ROOT = 0
    }
}