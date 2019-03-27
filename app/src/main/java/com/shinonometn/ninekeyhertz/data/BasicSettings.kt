package com.shinonometn.ninekeyhertz.data

import android.content.Context
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File

class BasicSettings : NamedSettings{

    override val name = "settings"

    var disableService: Boolean = false
    var showFloatWindow: Boolean = false
    var verboseToast : Boolean = true

    companion object {

        private val objectMapper = ObjectMapper()

        private val filename = "settings.json"

        fun loadFromJsonFile(context: Context): BasicSettings {
            val file = File(context.filesDir, "settings.json")
            if (!file.exists()) return BasicSettings()

            return objectMapper.readerFor(BasicSettings::class.java).readValue(file)
        }
    }
}